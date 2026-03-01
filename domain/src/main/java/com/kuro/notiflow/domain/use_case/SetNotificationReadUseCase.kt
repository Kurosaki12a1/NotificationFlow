package com.kuro.notiflow.domain.use_case

import com.kuro.notiflow.domain.api.notifications.NotificationRepository
import javax.inject.Inject

class SetNotificationReadUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(id: Long, isRead: Boolean) {
        repository.setRead(id, isRead)
    }
}
