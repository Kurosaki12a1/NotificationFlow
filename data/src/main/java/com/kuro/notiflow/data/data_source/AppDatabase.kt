package com.kuro.notiflow.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kuro.notiflow.data.data_source.notification.NotificationDao
import com.kuro.notiflow.data.data_source.settings.SettingsDao
import com.kuro.notiflow.data.entity.NotificationEntity
import com.kuro.notiflow.data.entity.SettingsEntity

@Database(
    entities = [
        SettingsEntity::class,
        NotificationEntity::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(ListStringConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun fetchSettingsDao(): SettingsDao
    abstract fun fetchNotificationDao(): NotificationDao

    companion object {
        const val DB_NAME = "notification_flow_db"
        const val DEFAULT_RETENTION_DAYS = 90
        const val INIT_DATABASE_SQL =
            "INSERT INTO settings_table (id, language, theme_colors, colors_type, dynamic_color, secure_mode, data_retention_days) VALUES (1,'DEFAULT', 'DEFAULT', 'BLUE', 0, 0, $DEFAULT_RETENTION_DAYS)"
    }
}
