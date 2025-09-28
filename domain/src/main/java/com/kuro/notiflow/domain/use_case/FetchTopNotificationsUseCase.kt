package com.kuro.notiflow.domain.use_case

import com.kuro.notiflow.domain.api.notifications.NotificationRepository
import javax.inject.Inject

class FetchTopNotificationsUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    operator fun invoke() = repository.fetchTopRecentNotifications()
}