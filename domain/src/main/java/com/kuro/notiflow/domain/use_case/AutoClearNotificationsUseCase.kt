package com.kuro.notiflow.domain.use_case

import com.kuro.notiflow.domain.api.notifications.NotificationRepository
import com.kuro.notiflow.domain.api.settings.SettingsMenuRepository
import com.kuro.notiflow.domain.models.settings.SettingsModel
import com.kuro.notiflow.domain.utils.handle
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class AutoClearNotificationsUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository,
    private val settingsRepository: SettingsMenuRepository
) {
    suspend operator fun invoke(currentTimeMillis: Long = System.currentTimeMillis()) {
        val settingsResult = settingsRepository.fetchAllSettings().first()
        settingsResult.handle { settings ->
            val retentionDays = settings?.dataRetentionDays ?: SettingsModel().dataRetentionDays
            if (retentionDays <= 0) return@handle

            val cutoffTime = currentTimeMillis - retentionDays.toLong() * MILLIS_PER_DAY
            notificationRepository.deleteOlderThan(cutoffTime)
        }
    }

    companion object {
        private const val MILLIS_PER_DAY = 24L * 60 * 60 * 1000
    }
}
