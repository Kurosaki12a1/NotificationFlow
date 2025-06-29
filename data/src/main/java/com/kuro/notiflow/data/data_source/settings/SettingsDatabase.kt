package com.kuro.notiflow.data.data_source.settings

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kuro.notiflow.data.data_source.settings.entity.SettingsEntity

@Database(
    entities = [
        SettingsEntity::class,
    ],
    version = 1,
    exportSchema = true
)
abstract class SettingsDatabase : RoomDatabase() {
    abstract fun fetchSettingsDao(): SettingsDao

    companion object {
        const val DB_NAME = "settings_db"
    }
}