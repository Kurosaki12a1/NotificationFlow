package com.kuro.notiflow.data.data_source.notification

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kuro.notiflow.data.entity.NotificationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(notification: NotificationEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(notifications: List<NotificationEntity>)

    @Query("SELECT * FROM notification_table ORDER BY postTime DESC")
    suspend fun getAll(): List<NotificationEntity>

    @Query("SELECT * FROM notification_table ORDER BY postTime DESC")
    fun fetchAll(): Flow<List<NotificationEntity>>

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
}