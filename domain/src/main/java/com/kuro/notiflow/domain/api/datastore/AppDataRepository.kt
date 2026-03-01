package com.kuro.notiflow.domain.api.datastore

import com.kuro.notiflow.domain.models.notifications.NotificationFilterSettings
import kotlinx.coroutines.flow.Flow

interface AppDataRepository {
    val isFirstLaunch: Flow<Boolean>
    val notificationFilterSettings: Flow<NotificationFilterSettings>
    suspend fun setOnboardingCompleted()
    suspend fun updateNotificationFilterSettings(settings: NotificationFilterSettings)
}
