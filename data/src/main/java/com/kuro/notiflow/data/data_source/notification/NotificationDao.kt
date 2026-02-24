package com.kuro.notiflow.data.data_source.notification

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kuro.notiflow.data.data_source.entity.NotificationEntity
import com.kuro.notiflow.domain.Constants
import com.kuro.notiflow.domain.models.notifications.NotificationStats
import com.kuro.notiflow.domain.models.notifications.PackageStats
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(notification: NotificationEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(notifications: List<NotificationEntity>)

    @Query("SELECT * FROM ${Constants.Database.NOTIFICATION_TABLE} WHERE id = :id")
    suspend fun getById(id: Long): NotificationEntity?

    @Query("SELECT * FROM ${Constants.Database.NOTIFICATION_TABLE} ORDER BY postTime DESC")
    suspend fun getAll(): List<NotificationEntity>

    @Query("SELECT * FROM ${Constants.Database.NOTIFICATION_TABLE} ORDER BY postTime DESC")
    fun fetchAll(): PagingSource<Int, NotificationEntity>

    @Query("SELECT * FROM ${Constants.Database.NOTIFICATION_TABLE} WHERE isBookmarked = 1 ORDER BY postTime DESC")
    fun fetchBookmarked(): PagingSource<Int, NotificationEntity>

    @Query("SELECT * FROM ${Constants.Database.NOTIFICATION_TABLE} WHERE packageName = :pkg ORDER BY postTime DESC")
    suspend fun getByPackage(pkg: String): List<NotificationEntity>

    @Query("SELECT * FROM ${Constants.Database.NOTIFICATION_TABLE} WHERE packageName = :pkg ORDER BY ABS(postTime -:targetTime) ASC LIMIT 1")
    suspend fun getRecentByPackage(pkg: String, targetTime: Long): NotificationEntity?

    @Query("DELETE FROM ${Constants.Database.NOTIFICATION_TABLE}")
    suspend fun clearAll()

    @Delete
    suspend fun delete(notification: NotificationEntity)

    @Query("DELETE FROM ${Constants.Database.NOTIFICATION_TABLE} WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM ${Constants.Database.NOTIFICATION_TABLE} WHERE postTime < :cutoffTime")
    suspend fun deleteOlderThan(cutoffTime: Long)

    @Query("UPDATE ${Constants.Database.NOTIFICATION_TABLE} SET isBookmarked = :isBookmarked WHERE id = :id")
    suspend fun updateBookmark(id: Long, isBookmarked: Boolean)

    @Query(
        """
    SELECT packageName,
           COUNT(*) AS count,
           (COUNT(*) * 100.0 / (SELECT COUNT(*) FROM ${Constants.Database.NOTIFICATION_TABLE})) AS percentage
    FROM ${Constants.Database.NOTIFICATION_TABLE}
    GROUP BY packageName
    ORDER BY count DESC
    LIMIT 5
"""
    )
    fun getTopPackages(): Flow<List<PackageStats>>

    @Query(
        """
    SELECT 
        (SELECT COUNT(*) FROM ${Constants.Database.NOTIFICATION_TABLE}) AS totalCount,
        (SELECT COUNT(*) FROM ${Constants.Database.NOTIFICATION_TABLE} WHERE isRead = 0) AS unreadCount,
        (SELECT COUNT(*) FROM ${Constants.Database.NOTIFICATION_TABLE} WHERE postTime BETWEEN :startOfDay AND :endOfDay) AS todayCount,
        (SELECT COUNT(*) FROM ${Constants.Database.NOTIFICATION_TABLE} WHERE postTime BETWEEN :startLastWeek AND :endLastWeek) AS lastWeekCount,
        (SELECT COUNT(*) FROM ${Constants.Database.NOTIFICATION_TABLE} WHERE postTime >= :startThisWeek) AS thisWeekCount
"""
    )
    fun getNotificationStats(
        startOfDay: Long,
        endOfDay: Long,
        startLastWeek: Long,
        endLastWeek: Long,
        startThisWeek: Long
    ): Flow<NotificationStats>
}
