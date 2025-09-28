package com.kuro.notiflow.data.data_source.notification

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.kuro.notiflow.data.entity.NotificationEntity
import com.kuro.notiflow.domain.models.notifications.NotificationStats
import com.kuro.notiflow.domain.models.notifications.PackageStats
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters

interface NotificationLocalDataSource {
    suspend fun addNotification(notification: NotificationEntity)
    suspend fun addNotifications(notifications: List<NotificationEntity>)
    suspend fun getNotificationById(id: Long): NotificationEntity?
    suspend fun getAllNotifications(): List<NotificationEntity>
    fun fetchAllNotifications(): Flow<PagingData<NotificationEntity>>
    fun fetchTopRecentNotifications(): Flow<List<PackageStats>>
    fun getNotificationsStats(): Flow<NotificationStats>
    suspend fun getNotificationsByPackage(pkg: String): List<NotificationEntity>
    suspend fun getRecentNotificationByPackage(pkg: String, targetTime: Long): NotificationEntity?
    suspend fun deleteNotification(notification: NotificationEntity)
    suspend fun deleteNotificationById(id: Long)
    suspend fun clearAll()
}

class NotificationLocalDataSourceImpl(private val dao: NotificationDao) :
    NotificationLocalDataSource {

    companion object {
        private const val ITEM_PER_PAGE = 50
    }

    private val zone = ZoneId.systemDefault()
    private val now = LocalDate.now(zone)

    private val startOfDay = now.atStartOfDay(zone).toInstant().toEpochMilli()
    private val endOfDay = now.plusDays(1).atStartOfDay(zone).toInstant().toEpochMilli() - 1

    private val mondayThisWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    private val startThisWeek = mondayThisWeek.atStartOfDay(zone).toInstant().toEpochMilli()

    private val mondayLastWeek = mondayThisWeek.minusWeeks(1)
    private val startLastWeek = mondayLastWeek.atStartOfDay(zone).toInstant().toEpochMilli()
    private val endLastWeek = mondayThisWeek.atStartOfDay(zone).toInstant().toEpochMilli() - 1

    override suspend fun addNotification(notification: NotificationEntity) {
        dao.insert(notification)
    }

    override suspend fun addNotifications(notifications: List<NotificationEntity>) {
        dao.insertAll(notifications)
    }

    override suspend fun getNotificationById(id: Long): NotificationEntity? {
        return dao.getById(id)
    }

    override suspend fun getAllNotifications(): List<NotificationEntity> {
        return dao.getAll()
    }

    override fun fetchAllNotifications(): Flow<PagingData<NotificationEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = ITEM_PER_PAGE,
                enablePlaceholders = false
            ), pagingSourceFactory = {
                dao.fetchAll()
            }
        ).flow.flowOn(Dispatchers.IO)
    }

    override fun fetchTopRecentNotifications(): Flow<List<PackageStats>> {
        return dao.getTopPackages()
    }

    override fun getNotificationsStats(): Flow<NotificationStats> {
        return dao.getNotificationStats(
            startOfDay = startOfDay,
            endOfDay = endOfDay,
            startLastWeek = startLastWeek,
            endLastWeek = endLastWeek,
            startThisWeek = startThisWeek
        )
    }

    override suspend fun getNotificationsByPackage(pkg: String): List<NotificationEntity> {
        return dao.getByPackage(pkg)
    }

    override suspend fun getRecentNotificationByPackage(
        pkg: String,
        targetTime: Long
    ): NotificationEntity? {
        return dao.getRecentByPackage(pkg, targetTime)
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