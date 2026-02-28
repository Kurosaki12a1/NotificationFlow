package com.kuro.notiflow.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kuro.notiflow.data.data_source.bookmark.BookmarkRuleDao
import com.kuro.notiflow.data.data_source.entity.BookmarkRuleEntity
import com.kuro.notiflow.data.data_source.notification.NotificationDao
import com.kuro.notiflow.data.data_source.settings.SettingsDao
import com.kuro.notiflow.data.data_source.entity.NotificationEntity
import com.kuro.notiflow.data.data_source.entity.SettingsEntity

@Database(
    entities = [
        SettingsEntity::class,
        NotificationEntity::class,
        BookmarkRuleEntity::class
    ],
    version = 3,
    exportSchema = false
)
@TypeConverters(ListStringConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun fetchSettingsDao(): SettingsDao
    abstract fun fetchNotificationDao(): NotificationDao
    abstract fun fetchBookmarkRuleDao(): BookmarkRuleDao
}
