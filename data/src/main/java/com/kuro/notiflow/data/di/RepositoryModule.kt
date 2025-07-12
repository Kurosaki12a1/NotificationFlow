package com.kuro.notiflow.data.di

import com.kuro.notiflow.data.data_source.notification.NotificationLocalDataSource
import com.kuro.notiflow.data.data_source.settings.SettingsLocalDataSource
import com.kuro.notiflow.data.impl.NotificationRepositoryImpl
import com.kuro.notiflow.data.impl.SettingsMenuRepositoryImpl
import com.kuro.notiflow.domain.api.notifications.NotificationRepository
import com.kuro.notiflow.domain.api.settings.SettingsMenuRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideSettingsMenuRepository(
        dataSource: SettingsLocalDataSource
    ): SettingsMenuRepository = SettingsMenuRepositoryImpl(dataSource)

    @Provides
    @Singleton
    fun provideNotificationRepository(
        dataSource: NotificationLocalDataSource
    ): NotificationRepository = NotificationRepositoryImpl(dataSource)
}