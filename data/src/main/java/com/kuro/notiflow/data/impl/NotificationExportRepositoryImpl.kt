package com.kuro.notiflow.data.impl

import com.kuro.notiflow.data.framework.exporter.ExportFileWriter
import com.kuro.notiflow.data.framework.exporter.NotificationCsvExporter
import com.kuro.notiflow.domain.api.export.NotificationExportRepository
import com.kuro.notiflow.domain.utils.AppLog
import com.kuro.notiflow.domain.models.export.ExportResult
import com.kuro.notiflow.domain.models.notifications.NotificationModel
import com.kuro.notiflow.domain.utils.wrap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 *
 * This repository coordinates the process of converting a list of [NotificationModel] objects
 * into a CSV format and writing them to a user-specified location via a URI. It delegates
 * the specific tasks of CSV generation and file writing to dedicated components.
 *
 * @property exporter The component responsible for converting notification models into a CSV data stream.
 * @property fileWriter The component that handles opening an output stream to a given URI.
 */
class NotificationExportRepositoryImpl @Inject constructor(
    private val exporter: NotificationCsvExporter,
    private val fileWriter: ExportFileWriter
) : NotificationExportRepository {


    /**
     * Exports a list of notifications to a specified file URI as a CSV.
     *
     * This operation is executed on the [Dispatchers.IO] context to ensure that file I/O
     * does not block the main thread. It uses the `use` extension function to guarantee
     * that the output stream is closed automatically, preventing resource leaks.
     *
     * @param notifications The list of [NotificationModel] instances to be exported.
     * @param targetUriString The string representation of the content URI where the file will be written.
     * @param fileName The name of the file being created.
     * @return A [Result] containing an [ExportResult] on success, which includes details
     *         about the created file. On failure, it returns a [Result] with an exception,
     *         for instance, if the output stream could not be opened.
     */
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
            // Open an output stream to the target URI. The 'use' block ensures it's closed.
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
