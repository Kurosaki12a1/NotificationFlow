package com.kuro.notiflow.data.impl

import com.kuro.notiflow.data.export.ExportFileWriter
import com.kuro.notiflow.data.export.NotificationCsvExporter
import com.kuro.notiflow.domain.models.export.ExportResult
import com.kuro.notiflow.domain.models.notifications.NotificationModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.ByteArrayOutputStream

class NotificationExportRepositoryImplTest {

    @Test
    fun `exports to target uri and returns result`() = runBlocking {
        val exporter = mockk<NotificationCsvExporter>(relaxed = true)
        val outputStream = ByteArrayOutputStream()
        val fileWriter = mockk<ExportFileWriter>()
        val repository = NotificationExportRepositoryImpl(exporter, fileWriter)

        val notifications = listOf(
            NotificationModel(
                id = 1,
                packageName = "com.test",
                title = "Title",
                text = "Text",
                subText = null,
                bigText = null,
                summaryText = null,
                infoText = null,
                textLines = null,
                postTime = 1_700_000_000_000,
                priority = 3,
                category = "message",
                smallIconResId = null,
                iconBase64 = null,
                groupKey = null,
                channelId = null,
                isRead = false,
                isBookmarked = false
            )
        )
        val targetUri = "content://test/file.csv"
        val fileName = "file.csv"
        every { fileWriter.openOutputStream(targetUri) } returns outputStream

        val result = repository.exportNotifications(
            notifications = notifications,
            targetUriString = targetUri,
            fileName = fileName
        )

        assertEquals(
            ExportResult(uriString = targetUri, fileName = fileName, totalCount = notifications.size),
            result.getOrNull()
        )
        assertTrue(result.isSuccess)
    }

    @Test
    fun `returns failure when output stream is null`() = runBlocking {
        val exporter = mockk<NotificationCsvExporter>(relaxed = true)
        val fileWriter = mockk<ExportFileWriter>()
        val repository = NotificationExportRepositoryImpl(exporter, fileWriter)

        val notifications = emptyList<NotificationModel>()
        val targetUri = "content://test/file.csv"

        every { fileWriter.openOutputStream(targetUri) } returns null

        val result = repository.exportNotifications(
            notifications = notifications,
            targetUriString = targetUri,
            fileName = "file.csv"
        )

        assertTrue(result.isFailure)
    }
}
