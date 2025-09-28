package com.kuro.notiflow.data.data_source.notification

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kuro.notiflow.data.entity.NotificationEntity
import com.kuro.notiflow.domain.models.notifications.NotificationStats
import com.kuro.notiflow.domain.models.notifications.PackageStats
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(notification: NotificationEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(notifications: List<NotificationEntity>)

    @Query("SELECT * FROM notification_table WHERE id = :id")
    suspend fun getById(id: Long): NotificationEntity?

    @Query("SELECT * FROM notification_table ORDER BY postTime DESC")
    suspend fun getAll(): List<NotificationEntity>

    @Query("SELECT * FROM notification_table ORDER BY postTime DESC")
    fun fetchAll(): PagingSource<Int, NotificationEntity>

    @Query("SELECT * FROM notification_table WHERE packageName = :pkg ORDER BY postTime DESC")
    suspend fun getByPackage(pkg: String): List<NotificationEntity>

    @Query("SELECT * FROM notification_table WHERE packageName = :pkg ORDER BY ABS(postTime -:targetTime) ASC LIMIT 1")
    suspend fun getRecentByPackage(pkg: String, targetTime: Long): NotificationEntity?

    @Query("DELETE FROM notification_table")
    suspend fun clearAll()

    @Delete
    suspend fun delete(notification: NotificationEntity)

    @Query("DELETE FROM notification_table WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query(
        """
    SELECT packageName,
           COUNT(*) AS count,
           (COUNT(*) * 100.0 / (SELECT COUNT(*) FROM notification_table)) AS percentage
    FROM notification_table
    GROUP BY packageName
    ORDER BY count DESC
    LIMIT 5
"""
    )
    fun getTopPackages(): Flow<List<PackageStats>>

    @Query(
        """
    SELECT 
        (SELECT COUNT(*) FROM notification_table) AS totalCount,
        (SELECT COUNT(*) FROM notification_table WHERE isRead = 0) AS unreadCount,
        (SELECT COUNT(*) FROM notification_table WHERE postTime BETWEEN :startOfDay AND :endOfDay) AS todayCount,
        (SELECT COUNT(*) FROM notification_table WHERE postTime BETWEEN :startLastWeek AND :endLastWeek) AS lastWeekCount,
        (SELECT COUNT(*) FROM notification_table WHERE postTime >= :startThisWeek) AS thisWeekCount
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