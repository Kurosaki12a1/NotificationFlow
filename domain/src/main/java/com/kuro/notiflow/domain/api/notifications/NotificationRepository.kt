package com.kuro.notiflow.domain.api.notifications

import androidx.paging.PagingData
import com.kuro.notiflow.domain.models.notifications.NotificationModel
import com.kuro.notiflow.domain.models.notifications.NotificationStats
import com.kuro.notiflow.domain.models.notifications.PackageStats
import kotlinx.coroutines.flow.Flow


interface NotificationRepository {

    suspend fun addNotification(notification: NotificationModel)

    suspend fun addNotifications(notifications: List<NotificationModel>)

    suspend fun getNotificationById(id: Long): Result<NotificationModel?>

    suspend fun getAllNotifications(): Result<List<NotificationModel>>

    fun fetchAllNotifications(): Flow<PagingData<NotificationModel>>

    fun fetchBookmarkedNotifications(): Flow<PagingData<NotificationModel>>

    fun fetchTopRecentNotifications(): Flow<List<PackageStats>>

    fun getNotificationsStats(): Flow<Result<NotificationStats>>

    suspend fun getNotificationsByPackage(pkg: String): Result<List<NotificationModel>>

    suspend fun deleteNotification(notification: NotificationModel)

    suspend fun deleteNotificationById(id: Long)

    suspend fun deleteOlderThan(cutoffTime: Long)

    suspend fun setBookmarked(id: Long, isBookmarked: Boolean)

    suspend fun clearAll()
}
