package com.kuro.notiflow.domain.use_case

import com.kuro.notiflow.domain.api.export.NotificationExportRepository
import com.kuro.notiflow.domain.api.notifications.NotificationRepository
import com.kuro.notiflow.domain.models.export.ExportResult
import javax.inject.Inject

class ExportNotificationsUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository,
    private val exportRepository: NotificationExportRepository
) {
    suspend operator fun invoke(
        targetUriString: String,
        fileName: String
    ): Result<ExportResult> {
        val notificationsResult = notificationRepository.getAllNotifications()
        val notifications = notificationsResult.getOrElse {
            return Result.failure(it)
        }
        return exportRepository.exportNotifications(
            notifications = notifications,
            targetUriString = targetUriString,
            fileName = fileName
        )
    }
}
