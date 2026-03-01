package com.kuro.notiflow.data.impl

import androidx.paging.PagingData
import androidx.paging.map
import com.kuro.notiflow.data.data_source.bookmark.BookmarkRuleLocalDataSource
import com.kuro.notiflow.data.data_source.notification.NotificationLocalDataSource
import com.kuro.notiflow.data.data_source.entity.BookmarkRuleEntity
import com.kuro.notiflow.data.mapper.toDomain
import com.kuro.notiflow.data.mapper.toEntity
import com.kuro.notiflow.domain.Constants
import com.kuro.notiflow.domain.api.notifications.NotificationRepository
import com.kuro.notiflow.domain.models.bookmark.BookmarkRuleMatchField
import com.kuro.notiflow.domain.models.bookmark.BookmarkRuleMatchType
import com.kuro.notiflow.domain.models.notifications.NotificationModel
import com.kuro.notiflow.domain.models.notifications.NotificationStats
import com.kuro.notiflow.domain.models.notifications.PackageStats
import com.kuro.notiflow.domain.utils.AppLog
import com.kuro.notiflow.domain.utils.wrap
import com.kuro.notiflow.domain.utils.wrapFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val dataSource: NotificationLocalDataSource,
    private val bookmarkRuleDataSource: BookmarkRuleLocalDataSource
) : NotificationRepository {

    /**
     * Insert a single [notification] if it passes the duplicate-filter rules described above.
     */
    override suspend fun addNotification(notification: NotificationModel) {
        AppLog.i(
            TAG,
            "addNotification pkg=${notification.packageName} time=${notification.postTime} " +
                "priority=${notification.priority}"
        )
        val recent = dataSource.getRecentNotificationByPackage(
            notification.packageName,
            notification.postTime
        )
        val shouldInsert = when {
            recent == null -> true
            recent.text != notification.text -> true
            notification.postTime - recent.postTime >=
                Constants.Notifications.MIN_INSERT_INTERVAL_MILLIS -> true
            else -> false
        }
        if (!shouldInsert) return
        val bookmarkRules = bookmarkRuleDataSource.getEnabledRules()
        dataSource.addNotification(notification.toAutoBookmarkEntity(bookmarkRules))
    }

    override suspend fun addNotifications(notifications: List<NotificationModel>) {
        AppLog.i(TAG, "addNotifications: ${notifications.size}")
        val bookmarkRules = bookmarkRuleDataSource.getEnabledRules()
        dataSource.addNotifications(notifications.map { it.toAutoBookmarkEntity(bookmarkRules) })
    }

    override suspend fun getNotificationById(id: Long): Result<NotificationModel?> {
        AppLog.d(TAG, "getNotificationById: $id")
        return wrap { dataSource.getNotificationById(id)?.toDomain() }
    }

    override suspend fun getAllNotifications(): Result<List<NotificationModel>> = wrap {
        AppLog.d(TAG, "getAllNotifications")
        dataSource.getAllNotifications().map { it.toDomain() }
    }

    override fun fetchAllNotifications(): Flow<PagingData<NotificationModel>> {
        AppLog.d(TAG, "fetchAllNotifications")
        return dataSource.fetchAllNotifications().map { paging -> paging.map { it.toDomain() } }
    }

    override fun fetchBookmarkedNotifications(): Flow<PagingData<NotificationModel>> {
        AppLog.d(TAG, "fetchBookmarkedNotifications")
        return dataSource.fetchBookmarkedNotifications().map { paging -> paging.map { it.toDomain() } }
    }

    override fun fetchTopRecentNotifications(): Flow<List<PackageStats>> {
        AppLog.d(TAG, "fetchTopRecentNotifications")
        return dataSource.fetchTopRecentNotifications()
    }

    override fun getNotificationsStats(): Flow<Result<NotificationStats>> = wrapFlow {
        AppLog.d(TAG, "getNotificationsStats")
        dataSource.getNotificationsStats()
    }

    override suspend fun getNotificationsByPackage(pkg: String): Result<List<NotificationModel>> =
        wrap {
            AppLog.d(TAG, "getNotificationsByPackage: $pkg")
            dataSource.getNotificationsByPackage(pkg).map { it.toDomain() }
        }

    override suspend fun deleteNotification(notification: NotificationModel) {
        AppLog.i(
            TAG,
            "deleteNotification id=${notification.id} pkg=${notification.packageName}"
        )
        dataSource.deleteNotification(notification.toEntity())
    }

    override suspend fun deleteNotificationById(id: Long) {
        AppLog.i(TAG, "deleteNotificationById id=$id")
        dataSource.deleteNotificationById(id)
    }

    override suspend fun deleteOlderThan(cutoffTime: Long) {
        AppLog.i(TAG, "deleteOlderThan cutoff=$cutoffTime")
        dataSource.deleteOlderThan(cutoffTime)
    }

    override suspend fun setBookmarked(id: Long, isBookmarked: Boolean) {
        AppLog.i(TAG, "setBookmarked id=$id value=$isBookmarked")
        dataSource.setBookmarked(id, isBookmarked)
    }

    override suspend fun setRead(id: Long, isRead: Boolean) {
        AppLog.i(TAG, "setRead id=$id value=$isRead")
        dataSource.setRead(id, isRead)
    }

    override suspend fun clearAll() {
        AppLog.i(TAG, "clearAll")
        dataSource.clearAll()
    }

    companion object {
        private const val TAG = "NotificationRepositoryImpl"
    }
}

private fun NotificationModel.toAutoBookmarkEntity(
    rules: List<BookmarkRuleEntity>
) = copy(isBookmarked = isBookmarked || matchesAnyRule(rules)).toEntity()

private fun NotificationModel.matchesAnyRule(rules: List<BookmarkRuleEntity>): Boolean {
    return rules.any { rule ->
        val packageMatches = rule.packageName.isNullOrBlank() || rule.packageName == packageName
        packageMatches && matchesRuleKeyword(rule)
    }
}

private fun NotificationModel.matchesRuleKeyword(rule: BookmarkRuleEntity): Boolean {
    // A package-only rule intentionally bookmarks every notification from that app.
    if (rule.keyword.isBlank()) return true

    val matchField = rule.matchField.toBookmarkRuleMatchField()
    val source = when (matchField) {
        BookmarkRuleMatchField.TITLE -> title.orEmpty()
        BookmarkRuleMatchField.TEXT -> text.orEmpty()
        else -> listOf(title, text).filterNotNull().joinToString("\n")
    }
    if (source.isBlank()) return false
    val candidate = source.lowercase()
    val keyword = rule.keyword.trim().lowercase()
    val matchType = rule.matchType.toBookmarkRuleMatchType()
    return when (matchType) {
        BookmarkRuleMatchType.EQUALS -> candidate == keyword
        BookmarkRuleMatchType.STARTS_WITH -> candidate.startsWith(keyword)
        else -> candidate.contains(keyword)
    }
}

private fun String.toBookmarkRuleMatchField(): BookmarkRuleMatchField {
    return runCatching { BookmarkRuleMatchField.valueOf(this) }
        .getOrDefault(BookmarkRuleMatchField.TITLE_OR_TEXT)
}

private fun String.toBookmarkRuleMatchType(): BookmarkRuleMatchType {
    return runCatching { BookmarkRuleMatchType.valueOf(this) }
        .getOrDefault(BookmarkRuleMatchType.CONTAINS)
}
