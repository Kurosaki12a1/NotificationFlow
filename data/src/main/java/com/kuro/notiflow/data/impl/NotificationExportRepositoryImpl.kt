package com.kuro.notiflow.data.impl

import com.kuro.notiflow.data.export.ExportFileWriter
import com.kuro.notiflow.data.export.NotificationCsvExporter
import com.kuro.notiflow.domain.api.export.NotificationExportRepository
import com.kuro.notiflow.domain.logger.AppLog
import com.kuro.notiflow.domain.models.export.ExportResult
import com.kuro.notiflow.domain.models.notifications.NotificationModel
import com.kuro.notiflow.domain.utils.wrap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NotificationExportRepositoryImpl @Inject constructor(
    private val exporter: NotificationCsvExporter,
    private val fileWriter: ExportFileWriter
) : NotificationExportRepository {

    override suspend fun exportNotifications(
        notifications: List<NotificationModel>,
        targetUriString: String,
        fileName: String
    ): Result<ExportResult> = withContext(Dispatchers.IO) {
        wrap {
            AppLog.i(
                TAG,
                "exportNotifications count=${notifications.size} file=$fileName uri=$targetUriString"
            )
            fileWriter.openOutputStream(targetUriString)?.use { output ->
                exporter.export(notifications, output)
            } ?: error("Failed to open export output stream")

            ExportResult(
                uriString = targetUriString,
                fileName = fileName,
                totalCount = notifications.size
            )
        }
    }

    companion object {
        private const val TAG = "NotificationExportRepositoryImpl"
    }
}
