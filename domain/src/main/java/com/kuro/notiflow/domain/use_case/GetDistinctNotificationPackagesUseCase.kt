package com.kuro.notiflow.domain.use_case

import com.kuro.notiflow.domain.api.notifications.NotificationRepository
import javax.inject.Inject

class GetDistinctNotificationPackagesUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(): List<String> = repository.getDistinctPackageNames()
}
