package com.kuro.notiflow.data.impl

import com.kuro.notiflow.data.data_source.bookmark.BookmarkRuleLocalDataSource
import com.kuro.notiflow.data.data_source.notification.NotificationLocalDataSource
import com.kuro.notiflow.data.data_source.entity.BookmarkRuleEntity
import com.kuro.notiflow.data.framework.app.AppInfoResolver
import com.kuro.notiflow.data.mapper.toDomain
import com.kuro.notiflow.data.mapper.toEntity
import com.kuro.notiflow.domain.api.bookmark.BookmarkRuleRepository
import com.kuro.notiflow.domain.models.app.AppSelectionItem
import com.kuro.notiflow.domain.models.bookmark.BookmarkRule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Persists bookmark rules and prepares app choices for the bookmark rule editor.
 *
 * This repository enforces the hard overlap guard before writes. App labels
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
     * Throws [IllegalArgumentException] if a conflicting rule already covers the
     * same match space.
     *
     * @param rule The [BookmarkRule] to be saved.
     * @return The row ID of the inserted/updated rule.
     */
    override suspend fun upsertRule(rule: BookmarkRule): Long {
        val candidate = rule.toEntity()
        // Keep the repository as the hard guard so overlapping rules cannot be
        // persisted even if another caller skips UI validation.
        if (ruleDataSource.getAllRules().hasConflict(candidate)) {
            throw IllegalArgumentException("Conflicting bookmark rule")
        }
        return ruleDataSource.upsertRule(candidate)
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

    private fun List<BookmarkRuleEntity>.hasConflict(candidate: BookmarkRuleEntity): Boolean {
        return any { existing ->
            existing.id != candidate.id &&
                existing.packageScopeOverlaps(candidate) &&
                existing.matchFieldScopeOverlaps(candidate) &&
                existing.keywordScopeOverlaps(candidate)
        }
    }

    private fun BookmarkRuleEntity.packageScopeOverlaps(other: BookmarkRuleEntity): Boolean {
        val packageName = packageName.orEmpty().trim()
        val otherPackageName = other.packageName.orEmpty().trim()
        return packageName.isEmpty() || otherPackageName.isEmpty() || packageName == otherPackageName
    }

    private fun BookmarkRuleEntity.matchFieldScopeOverlaps(other: BookmarkRuleEntity): Boolean {
        return matchField == other.matchField ||
            matchField == MATCH_FIELD_TITLE_OR_TEXT ||
            other.matchField == MATCH_FIELD_TITLE_OR_TEXT
    }

    private fun BookmarkRuleEntity.keywordScopeOverlaps(other: BookmarkRuleEntity): Boolean {
        val keyword = keyword.trim().lowercase()
        val otherKeyword = other.keyword.trim().lowercase()
        return keyword.isEmpty() || otherKeyword.isEmpty() || keyword == otherKeyword
    }
}

private const val MATCH_FIELD_TITLE_OR_TEXT = "TITLE_OR_TEXT"
