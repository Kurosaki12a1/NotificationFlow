package com.kuro.notiflow.data.impl

import androidx.paging.PagingData
import androidx.paging.map
import com.kuro.notiflow.data.data_source.notification.NotificationLocalDataSource
import com.kuro.notiflow.data.mapper.toDomain
import com.kuro.notiflow.data.mapper.toEntity
import com.kuro.notiflow.domain.Constants
import com.kuro.notiflow.domain.api.notifications.NotificationRepository
import com.kuro.notiflow.domain.models.notifications.NotificationModel
import com.kuro.notiflow.domain.models.notifications.NotificationStats
import com.kuro.notiflow.domain.models.notifications.PackageStats
import com.kuro.notiflow.domain.utils.wrap
import com.kuro.notiflow.domain.utils.wrapFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val dataSource: NotificationLocalDataSource
) : NotificationRepository {

    /**
     * Insert a single [notification] if it passes the duplicate-filter rules described above.
     */
    override suspend fun addNotification(notification: NotificationModel) {
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
        if (shouldInsert) dataSource.addNotification(notification.toEntity())
    }

    override suspend fun addNotifications(notifications: List<NotificationModel>) {
        dataSource.addNotifications(notifications.map { it.toEntity() })
    }

    override suspend fun getNotificationById(id: Long): Result<NotificationModel?> {
        return wrap { dataSource.getNotificationById(id)?.toDomain() }
    }

    override suspend fun getAllNotifications(): Result<List<NotificationModel>> = wrap {
        dataSource.getAllNotifications().map { it.toDomain() }
    }

    override fun fetchAllNotifications(): Flow<PagingData<NotificationModel>> {
        return dataSource.fetchAllNotifications().map { paging -> paging.map { it.toDomain() } }
    }

    override fun fetchTopRecentNotifications(): Flow<List<PackageStats>> {
        return dataSource.fetchTopRecentNotifications()
    }

    override fun getNotificationsStats(): Flow<Result<NotificationStats>> = wrapFlow {
        dataSource.getNotificationsStats()
    }

    override suspend fun getNotificationsByPackage(pkg: String): Result<List<NotificationModel>> =
        wrap { dataSource.getNotificationsByPackage(pkg).map { it.toDomain() } }

    override suspend fun deleteNotification(notification: NotificationModel) {
        dataSource.deleteNotification(notification.toEntity())
    }

    override suspend fun deleteNotificationById(id: Long) {
        dataSource.deleteNotificationById(id)
    }

    override suspend fun deleteOlderThan(cutoffTime: Long) {
        dataSource.deleteOlderThan(cutoffTime)
    }

    override suspend fun clearAll() {
        dataSource.clearAll()
    }
}
