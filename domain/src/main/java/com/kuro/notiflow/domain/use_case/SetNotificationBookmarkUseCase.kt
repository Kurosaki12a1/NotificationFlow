package com.kuro.notiflow.domain.use_case

import com.kuro.notiflow.domain.api.notifications.NotificationRepository
import javax.inject.Inject

class SetNotificationBookmarkUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(id: Long, isBookmarked: Boolean) {
        repository.setBookmarked(id, isBookmarked)
    }
}
