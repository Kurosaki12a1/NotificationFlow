package com.kuro.notiflow.data.impl

import android.content.Context
import com.kuro.notiflow.data.data_source.bookmark.BookmarkRuleLocalDataSource
import com.kuro.notiflow.data.data_source.notification.NotificationLocalDataSource
import com.kuro.notiflow.data.mapper.toDomain
import com.kuro.notiflow.data.mapper.toEntity
import com.kuro.notiflow.domain.api.bookmark.BookmarkRuleRepository
import com.kuro.notiflow.domain.models.app.AppSelectionItem
import com.kuro.notiflow.domain.models.bookmark.BookmarkRule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BookmarkRuleRepositoryImpl(
    private val ruleDataSource: BookmarkRuleLocalDataSource,
    private val notificationDataSource: NotificationLocalDataSource,
    private val context: Context
) : BookmarkRuleRepository {
    override fun fetchRules(): Flow<List<BookmarkRule>> {
        return ruleDataSource.fetchRules().map { rules -> rules.map { it.toDomain() } }
    }

    override suspend fun upsertRule(rule: BookmarkRule): Long {
        return ruleDataSource.upsertRule(rule.toEntity())
    }

    override suspend fun deleteRule(id: Long) {
        ruleDataSource.deleteRule(id)
    }

    override suspend fun fetchAvailableApps(): List<AppSelectionItem> {
        return notificationDataSource.getDistinctPackageNames()
            .map { packageName ->
                AppSelectionItem(
                    packageName = packageName,
                    appName = context.resolveAppName(packageName)
                )
            }
            .sortedBy { it.appName.lowercase() }
    }
}

private fun Context.resolveAppName(packageName: String): String {
    return runCatching {
        val appInfo = packageManager.getApplicationInfo(packageName, 0)
        packageManager.getApplicationLabel(appInfo).toString()
    }.getOrElse { packageName }
}
