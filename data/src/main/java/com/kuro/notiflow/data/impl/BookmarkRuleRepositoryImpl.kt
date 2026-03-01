package com.kuro.notiflow.data.impl

import com.kuro.notiflow.data.data_source.bookmark.BookmarkRuleLocalDataSource
import com.kuro.notiflow.data.data_source.notification.NotificationLocalDataSource
import com.kuro.notiflow.domain.api.app.AppInfoProvider
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
 * are resolved through [AppInfoProvider] so this class can stay testable
 * without depending on Android framework types directly.
 */
class BookmarkRuleRepositoryImpl(
    private val ruleDataSource: BookmarkRuleLocalDataSource,
    private val notificationDataSource: NotificationLocalDataSource,
    private val appInfoProvider: AppInfoProvider
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
        if (ruleDataSource.getAllRules().map { it.toDomain() }.hasConflict(rule)) {
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
                    appName = appInfoProvider.resolveAppName(packageName)
                )
            }
            .sortedBy { it.appName.lowercase() }
    }

    private fun List<BookmarkRule>.hasConflict(candidate: BookmarkRule): Boolean {
        return any { existing ->
            existing.id != candidate.id &&
                existing.packageScopeOverlaps(candidate.packageName.orEmpty().trim()) &&
                existing.matchFieldScopeOverlaps(candidate.matchField) &&
                existing.keywordScopeOverlaps(candidate.keyword.trim().lowercase())
        }
    }
}
