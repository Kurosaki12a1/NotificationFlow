package com.kuro.notiflow.domain.api.export

import com.kuro.notiflow.domain.models.export.ExportResult
import com.kuro.notiflow.domain.models.notifications.NotificationModel

interface NotificationExportRepository {
    suspend fun exportNotifications(
        notifications: List<NotificationModel>,
        targetUriString: String,
        fileName: String
    ): Result<ExportResult>
}
