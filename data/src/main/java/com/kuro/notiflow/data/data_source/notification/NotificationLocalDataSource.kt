package com.kuro.notiflow.data.data_source.notification

import com.kuro.notiflow.data.entity.NotificationEntity
import kotlinx.coroutines.flow.Flow

interface NotificationLocalDataSource {
    suspend fun addNotification(notification: NotificationEntity)
    suspend fun addNotifications(notifications: List<NotificationEntity>)
    suspend fun getAllNotifications(): List<NotificationEntity>
    fun fetchAllNotifications(): Flow<List<NotificationEntity>>
    suspend fun getNotificationsByPackage(pkg: String): List<NotificationEntity>
    suspend fun deleteNotification(notification: NotificationEntity)
    suspend fun deleteNotificationById(id: Long)
    suspend fun clearAll()
}

class NotificationLocalDataSourceImpl(private val dao: NotificationDao) : NotificationLocalDataSource {
    override suspend fun addNotification(notification: NotificationEntity) {
        dao.insert(notification)
    }

    override suspend fun addNotifications(notifications: List<NotificationEntity>) {
        dao.insertAll(notifications)
    }

    override suspend fun getAllNotifications(): List<NotificationEntity> {
        return dao.getAll()
    }

    override fun fetchAllNotifications(): Flow<List<NotificationEntity>> {
        return dao.fetchAll()
    }

    override suspend fun getNotificationsByPackage(pkg: String): List<NotificationEntity> {
        return dao.getByPackage(pkg)
    }

    override suspend fun deleteNotification(notification: NotificationEntity) {
        dao.delete(notification)
    }

    override suspend fun deleteNotificationById(id: Long) {
        dao.deleteById(id)
    }

    override suspend fun clearAll() {
        dao.clearAll()
    }
}