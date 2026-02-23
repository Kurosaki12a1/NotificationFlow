package com.kuro.notiflow.domain.use_case

import androidx.paging.PagingData
import com.kuro.notiflow.domain.api.notifications.NotificationRepository
import com.kuro.notiflow.domain.models.notifications.NotificationModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchBookmarkedNotificationsUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    operator fun invoke(): Flow<PagingData<NotificationModel>> =
        repository.fetchBookmarkedNotifications()
}
