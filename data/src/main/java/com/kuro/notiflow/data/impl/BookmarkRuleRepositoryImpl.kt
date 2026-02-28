package com.kuro.notiflow.data.impl

import com.kuro.notiflow.data.data_source.bookmark.BookmarkRuleLocalDataSource
import com.kuro.notiflow.data.data_source.entity.NotificationEntity
import com.kuro.notiflow.data.data_source.notification.NotificationLocalDataSource
import com.kuro.notiflow.data.data_source.entity.BookmarkRuleEntity
import com.kuro.notiflow.data.framework.app.AppInfoResolver
import com.kuro.notiflow.data.mapper.toDomain
import com.kuro.notiflow.data.mapper.toEntity
import com.kuro.notiflow.domain.api.bookmark.BookmarkRuleRepository
import com.kuro.notiflow.domain.models.app.AppSelectionItem
import com.kuro.notiflow.domain.models.bookmark.BookmarkRule
import com.kuro.notiflow.domain.models.bookmark.BookmarkRuleMatchField
import com.kuro.notiflow.domain.models.bookmark.BookmarkRuleMatchType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Persists bookmark rules and prepares app choices for the bookmark rule editor.
 *
 * This repository also enforces the hard duplicate guard before writes and
 * backfills existing notifications when an enabled rule is saved. App labels
 * are resolved through [AppInfoResolver] so this class can stay testable
 * without depending on Android framework types directly.
 */
class BookmarkRuleRepositoryImpl(
    private val ruleDataSource: BookmarkRuleLocalDataSource,
    private val notificationDataSource: NotificationLocalDataSource,
    private val appInfoResolver: AppInfoResolver
) : BookmarkRuleRepository {

    /**
     * Observes the list of all bookmark rules from the local data source.
     *
     * @return A [Flow] emitting updates to the list of rules mapped to domain models.
     */
    override fun fetchRules(): Flow<List<BookmarkRule>> {
        return ruleDataSource.fetchRules().map { rules -> rules.map { it.toDomain() } }
    }

    /**
     * Saves or updates a bookmark rule.
     *
     * Throws [IllegalArgumentException] if a duplicate rule (same package, keyword, and match types) exists.
     * If the rule is enabled, it triggers a backfill process for existing notifications.
     *
     * @param rule The [BookmarkRule] to be saved.
     * @return The row ID of the inserted/updated rule.
     */
    override suspend fun upsertRule(rule: BookmarkRule): Long {
        val candidate = rule.toEntity()
        // SUGGESTION: Fetching all rules to check for duplicates in-memory might not scale well
        // if the user has hundreds of rules. Consider a specific DAO query for existence.
        if (ruleDataSource.getAllRules().hasDuplicate(candidate)) {
            throw IllegalArgumentException("Duplicate bookmark rule")
        }
        val rowId = ruleDataSource.upsertRule(candidate)
        if (candidate.isEnabled) {
            applyRuleToExistingNotifications(candidate)
        }
        return rowId
    }

    /**
     * Deletes a bookmark rule by its unique identifier.
     *
     * @param id The ID of the rule to remove.
     */
    override suspend fun deleteRule(id: Long) {
        ruleDataSource.deleteRule(id)
    }

    /**
     * Retrieves a list of apps that have previously posted notifications.
     *
     * This is used to populate the app selection dropdown in the rule editor.
     *
     * @return A sorted list of [AppSelectionItem] containing package names and resolved app names.
     */
    override suspend fun fetchAvailableApps(): List<AppSelectionItem> {
        return notificationDataSource.getDistinctPackageNames()
            .map { packageName ->
                AppSelectionItem(
                    packageName = packageName,
                    appName = appInfoResolver.resolveAppName(packageName)
                )
            }
            .sortedBy { it.appName.lowercase() }
    }

    private suspend fun applyRuleToExistingNotifications(rule: BookmarkRuleEntity) {
        // Only add bookmarks here. Rule removal does not clear bookmarks because
        // users may have set them manually.
        // SUGGESTION: notificationDataSource.getAllNotifications() could return a very large
        // dataset. Filtering in-memory with a Sequence is safer than a List, but ideally,
        // this matching logic should be performed via a SQL query in the DAO for efficiency.
        notificationDataSource.getAllNotifications()
            .asSequence()
            .filterNot { it.isBookmarked }
            .filter { it.matchesRule(rule) }
            .forEach { notification ->
                notificationDataSource.setBookmarked(notification.id, true)
            }
    }

    private fun List<BookmarkRuleEntity>.hasDuplicate(candidate: BookmarkRuleEntity): Boolean {
        return any { existing ->
            existing.id != candidate.id &&
                    existing.packageName.orEmpty().trim() == candidate.packageName.orEmpty().trim() &&
                    existing.keyword.trim().equals(candidate.keyword.trim(), ignoreCase = true) &&
                    existing.matchField == candidate.matchField &&
                    existing.matchType == candidate.matchType
        }
    }

    private fun NotificationEntity.matchesRule(rule: BookmarkRuleEntity): Boolean {
        val packageMatches = rule.packageName.isNullOrBlank() || rule.packageName == packageName
        return packageMatches && matchesRuleKeyword(rule)
    }

    private fun NotificationEntity.matchesRuleKeyword(rule: BookmarkRuleEntity): Boolean {
        val source = when (rule.matchField.toBookmarkRuleMatchField()) {
            BookmarkRuleMatchField.TITLE -> title.orEmpty()
            BookmarkRuleMatchField.TEXT -> text.orEmpty()
            BookmarkRuleMatchField.TITLE_OR_TEXT -> listOfNotNull(title, text)
                .joinToString("\n")
        }
        if (source.isBlank()) return false
        val candidate = source.lowercase()
        val keyword = rule.keyword.trim().lowercase()
        if (keyword.isEmpty()) return false
        return when (rule.matchType.toBookmarkRuleMatchType()) {
            BookmarkRuleMatchType.EQUALS -> candidate == keyword
            BookmarkRuleMatchType.STARTS_WITH -> candidate.startsWith(keyword)
            BookmarkRuleMatchType.CONTAINS -> candidate.contains(keyword)
        }
    }

    // NIT: These extension functions are only used here. Consider moving them to
    // a Mapper or the Enum classes themselves to keep the Repository clean.
    private fun String.toBookmarkRuleMatchField(): BookmarkRuleMatchField {
        return runCatching { BookmarkRuleMatchField.valueOf(this) }
            .getOrDefault(BookmarkRuleMatchField.TITLE_OR_TEXT)
    }

    private fun String.toBookmarkRuleMatchType(): BookmarkRuleMatchType {
        return runCatching { BookmarkRuleMatchType.valueOf(this) }
            .getOrDefault(BookmarkRuleMatchType.CONTAINS)
    }
}