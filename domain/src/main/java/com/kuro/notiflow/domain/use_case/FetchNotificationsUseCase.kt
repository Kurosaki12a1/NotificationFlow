package com.kuro.notiflow.domain.use_case

import com.kuro.notiflow.domain.api.notifications.NotificationRepository
import com.kuro.notiflow.domain.models.notifications.NotificationModel
import com.kuro.notiflow.domain.utils.collectAndHandle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FetchNotificationsUseCase @Inject constructor(
    private val repository: NotificationRepository
) {

    operator fun invoke(): Flow<List<NotificationModel>> = flow {
        repository.fetchAllNotifications().collectAndHandle(
            onFailure = { emit(listOf()) },
            onSuccess = { emit(it ?: listOf()) }
        )
    }
}