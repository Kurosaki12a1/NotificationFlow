package com.kuro.notiflow.data.data_source.notification

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.kuro.notiflow.data.data_source.entity.NotificationEntity
import com.kuro.notiflow.domain.Constants
import com.kuro.notiflow.domain.models.notifications.NotificationStats
import com.kuro.notiflow.domain.models.notifications.PackageStats
import com.kuro.notiflow.domain.utils.TimeProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

interface NotificationLocalDataSource {
    suspend fun addNotification(notification: NotificationEntity)
    suspend fun addNotifications(notifications: List<NotificationEntity>)
    suspend fun getNotificationById(id: Long): NotificationEntity?
    suspend fun getAllNotifications(): List<NotificationEntity>
    fun fetchAllNotifications(): Flow<PagingData<NotificationEntity>>
    fun fetchBookmarkedNotifications(): Flow<PagingData<NotificationEntity>>
    fun fetchTopRecentNotifications(): Flow<List<PackageStats>>
    fun getNotificationsStats(): Flow<NotificationStats>
    suspend fun getNotificationsByPackage(pkg: String): List<NotificationEntity>
    suspend fun getRecentNotificationByPackage(pkg: String, targetTime: Long): NotificationEntity?
    suspend fun getDistinctPackageNames(): List<String>
    suspend fun deleteNotification(notification: NotificationEntity)
    suspend fun deleteNotificationById(id: Long)
    suspend fun deleteOlderThan(cutoffTime: Long)
    suspend fun setBookmarked(id: Long, isBookmarked: Boolean)
    suspend fun clearAll()
}

/**
 * Local data source implementation for managing notifications stored in Room.
 *
 * This class abstracts the access to the NotificationDao and provides
 * high-level methods to insert, query, and delete notifications.
 *
 * NOTE:
 * - Paging is supported via [fetchAllNotifications].
 * - Stats are pre-calculated based on system default timezone at init.
 *   Be careful: if the process lives across day boundaries, these values may
 *   become stale.
 */
class NotificationLocalDataSourceImpl @Inject constructor(
    private val dao: NotificationDao,
    private val timeProvider: TimeProvider
) : NotificationLocalDataSource {

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

    /**
     * Fetch all notifications with paging support.
     *
     * This is backed by Room's PagingSource.
     */

    override fun fetchAllNotifications(): Flow<PagingData<NotificationEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = Constants.Notifications.PAGE_SIZE,
                enablePlaceholders = false
            ), pagingSourceFactory = {
                dao.fetchAll()
            }
        ).flow.flowOn(Dispatchers.IO)
    }

    override fun fetchBookmarkedNotifications(): Flow<PagingData<NotificationEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = Constants.Notifications.PAGE_SIZE,
                enablePlaceholders = false
            ), pagingSourceFactory = {
                dao.fetchBookmarked()
            }
        ).flow.flowOn(Dispatchers.IO)
    }

    override fun fetchTopRecentNotifications(): Flow<List<PackageStats>> {
        return dao.getTopPackages()
    }

    /**
     * Get overall notification statistics:
     * - totalCount
     * - unreadCount
     * - todayCount
     * - lastWeekCount
     * - thisWeekCount
     *
     * NOTE: Uses precomputed time ranges from init.
     */
    override fun getNotificationsStats(): Flow<NotificationStats> {
        return dao.getNotificationStats(
            startOfDay = timeProvider.startOfDay(),
            endOfDay = timeProvider.endOfDay(),
            startLastWeek = timeProvider.startOfLastWeek(),
            endLastWeek = timeProvider.endOfLastWeek(),
            startThisWeek = timeProvider.startOfThisWeek()
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

    override suspend fun getDistinctPackageNames(): List<String> {
        return dao.getDistinctPackageNames()
    }

    override suspend fun deleteNotification(notification: NotificationEntity) {
        dao.delete(notification)
    }

    override suspend fun deleteNotificationById(id: Long) {
        dao.deleteById(id)
    }

    override suspend fun deleteOlderThan(cutoffTime: Long) {
        dao.deleteOlderThan(cutoffTime)
    }

    override suspend fun setBookmarked(id: Long, isBookmarked: Boolean) {
        dao.updateBookmark(id, isBookmarked)
    }

    override suspend fun clearAll() {
        dao.clearAll()
    }
}
