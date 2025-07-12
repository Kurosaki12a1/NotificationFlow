package com.kuro.notiflow.domain.api.notifications

import com.kuro.notiflow.domain.models.notifications.NotificationModel
import kotlinx.coroutines.flow.Flow


interface NotificationRepository {

    suspend fun addNotification(notification: NotificationModel)

    suspend fun addNotifications(notifications: List<NotificationModel>)

    suspend fun getAllNotifications(): List<NotificationModel>

    fun fetchAllNotifications(): Flow<List<NotificationModel>>

    suspend fun getNotificationsByPackage(pkg: String): List<NotificationModel>

    suspend fun deleteNotification(notification: NotificationModel)

    suspend fun deleteNotificationById(id: Long)

    suspend fun clearAll()
}