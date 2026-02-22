package com.kuro.notiflow.domain.use_case

import com.kuro.notiflow.domain.api.importer.NotificationImportRepository
import com.kuro.notiflow.domain.api.notifications.NotificationRepository
import javax.inject.Inject

class ImportNotificationsUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository,
    private val importRepository: NotificationImportRepository
) {
    suspend operator fun invoke(uriString: String): Result<Int> {
        val importedResult = importRepository.importNotifications(uriString)
        val imported = importedResult.getOrElse { return Result.failure(it) }
        if (imported.isEmpty()) {
            return Result.success(0)
        }
        notificationRepository.addNotifications(imported)
        return Result.success(imported.size)
    }
}
