package com.kuro.notiflow.data.data_source.settings

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.kuro.notiflow.data.entity.SettingsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingsDao {

    @Query("SELECT * FROM settings_table")
    fun fetchSettingsFlow(): Flow<SettingsEntity>

    @Update
    suspend fun updateSettings(entity: SettingsEntity) : Int
}
