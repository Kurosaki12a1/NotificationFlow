package com.kuro.notiflow.data.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import com.kuro.notiflow.data.data_source.AppDatabase
import com.kuro.notiflow.data.data_source.bookmark.BookmarkRuleDao
import com.kuro.notiflow.data.data_source.bookmark.BookmarkRuleLocalDataSource
import com.kuro.notiflow.data.data_source.bookmark.BookmarkRuleLocalDataSourceImpl
import com.kuro.notiflow.data.data_source.notification.NotificationDao
import com.kuro.notiflow.data.data_source.notification.NotificationLocalDataSource
import com.kuro.notiflow.data.data_source.notification.NotificationLocalDataSourceImpl
import com.kuro.notiflow.data.data_source.settings.SettingsDao
import com.kuro.notiflow.data.data_source.settings.SettingsLocalDataSource
import com.kuro.notiflow.data.data_source.settings.SettingsLocalDataSourceImpl
import com.kuro.notiflow.data.data_source.migration.Migration_1_2
import com.kuro.notiflow.data.data_source.migration.Migration_2_3
import com.kuro.notiflow.domain.Constants
import com.kuro.notiflow.domain.utils.TimeProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideSettingsDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder<AppDatabase>(
            context = context,
            name = Constants.Database.NAME
        ).setQueryCoroutineContext(Dispatchers.IO)
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(connection: SQLiteConnection) {
                    super.onCreate(connection)
                    connection.execSQL(Constants.Database.INIT_SETTINGS_SQL)
                }
            })
            .addMigrations(Migration_1_2)
            .addMigrations(Migration_2_3)
            .build()
    }

    @Provides
    @Singleton
    fun provideSettingsDao(database: AppDatabase): SettingsDao {
        return database.fetchSettingsDao()
    }

    @Provides
    @Singleton
    fun provideNotificationDao(database: AppDatabase): NotificationDao {
        return database.fetchNotificationDao()
    }

    @Provides
    @Singleton
    fun provideBookmarkRuleDao(database: AppDatabase): BookmarkRuleDao {
        return database.fetchBookmarkRuleDao()
    }

    @Provides
    @Singleton
    fun provideSettingsLocalDataSource(settingsDao: SettingsDao): SettingsLocalDataSource {
        return SettingsLocalDataSourceImpl(settingsDao)
    }

    @Provides
    @Singleton
    fun provideNotificationLocalDataSource(
        notificationDao: NotificationDao,
        timeProvider: TimeProvider
    ): NotificationLocalDataSource =
        NotificationLocalDataSourceImpl(notificationDao, timeProvider)

    @Provides
    @Singleton
    fun provideBookmarkRuleLocalDataSource(
        bookmarkRuleDao: BookmarkRuleDao
    ): BookmarkRuleLocalDataSource = BookmarkRuleLocalDataSourceImpl(bookmarkRuleDao)
}
