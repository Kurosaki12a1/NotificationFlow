package com.kuro.notiflow.domain.use_case

import com.kuro.notiflow.domain.api.notifications.NotificationRepository
import com.kuro.notiflow.domain.models.notifications.NotificationModel
import javax.inject.Inject

class GetNotificationUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(id: Long): NotificationModel? {
        return repository.getNotificationById(id).getOrNull()
    }
}