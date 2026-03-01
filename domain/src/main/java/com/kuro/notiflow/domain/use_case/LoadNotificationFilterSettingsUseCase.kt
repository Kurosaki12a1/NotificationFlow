package com.kuro.notiflow.domain.use_case

import com.kuro.notiflow.domain.api.datastore.AppDataRepository
import com.kuro.notiflow.domain.models.notifications.NotificationFilterSettings
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoadNotificationFilterSettingsUseCase @Inject constructor(
    private val repository: AppDataRepository
) {
    operator fun invoke(): Flow<NotificationFilterSettings> {
        return repository.notificationFilterSettings
    }
}
