package com.kuro.notiflow.data.di

import android.content.Context
import androidx.room.Room
import com.kuro.notiflow.data.data_source.settings.SettingsDao
import com.kuro.notiflow.data.data_source.settings.SettingsDatabase
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
    ): SettingsDatabase {
        return Room.databaseBuilder<SettingsDatabase>(
            context = context,
            name = SettingsDatabase.DB_NAME
        ).setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }

    @Provides
    @Singleton
    fun provideSettingsDao(database: SettingsDatabase): SettingsDao {
        return database.fetchSettingsDao()
    }
}