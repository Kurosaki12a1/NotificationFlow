package com.kuro.notiflow.domain.use_case

import com.kuro.notiflow.domain.api.notifications.NotificationRepository
import com.kuro.notiflow.domain.models.notifications.NotificationStats
import com.kuro.notiflow.domain.utils.collectAndHandle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetOverviewNotificationStatsUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    operator fun invoke(): Flow<NotificationStats> = flow {
        notificationRepository.getNotificationsStats().collectAndHandle(
            onFailure = { emit(NotificationStats()) },
            onSuccess = { emit(it ?: NotificationStats()) }
        )
    }

}