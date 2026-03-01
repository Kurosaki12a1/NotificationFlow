package com.kuro.notiflow.domain.use_case

import com.kuro.notiflow.domain.api.datastore.AppDataRepository
import com.kuro.notiflow.domain.models.notifications.NotificationFilterSettings
import javax.inject.Inject

class UpdateNotificationFilterSettingsUseCase @Inject constructor(
    private val repository: AppDataRepository
) {
    suspend operator fun invoke(settings: NotificationFilterSettings) {
        repository.updateNotificationFilterSettings(settings)
    }
}
