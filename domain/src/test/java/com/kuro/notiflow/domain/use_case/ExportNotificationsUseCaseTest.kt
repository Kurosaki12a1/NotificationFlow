package com.kuro.notiflow.domain.use_case

import com.kuro.notiflow.domain.api.export.NotificationExportRepository
import com.kuro.notiflow.domain.api.notifications.NotificationRepository
import com.kuro.notiflow.domain.models.export.ExportResult
import com.kuro.notiflow.domain.models.notifications.NotificationModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExportNotificationsUseCaseTest {

    private val notificationRepository = mockk<NotificationRepository>()
    private val exportRepository = mockk<NotificationExportRepository>()

    private val useCase = ExportNotificationsUseCase(
        notificationRepository = notificationRepository,
        exportRepository = exportRepository
    )

    @Test
    fun `returns failure when notifications load fails`() = runBlocking {
        val error = IllegalStateException("load failed")
        coEvery { notificationRepository.getAllNotifications() } returns Result.failure(error)

        val result = useCase("content://test", "file.csv")

        assertTrue(result.isFailure)
        assertEquals(error, result.exceptionOrNull())
        coVerify(exactly = 0) {
            exportRepository.exportNotifications(any(), any(), any())
        }
    }

    @Test
    fun `exports notifications when load succeeds`() = runBlocking {
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
        val exportResult = ExportResult(
            uriString = "content://export",
            fileName = "file.csv",
            totalCount = notifications.size
        )

        coEvery { notificationRepository.getAllNotifications() } returns Result.success(notifications)
        coEvery {
            exportRepository.exportNotifications(
                notifications = notifications,
                targetUriString = "content://export",
                fileName = "file.csv"
            )
        } returns Result.success(exportResult)

        val result = useCase("content://export", "file.csv")

        assertTrue(result.isSuccess)
        assertEquals(exportResult, result.getOrNull())
        coVerify(exactly = 1) {
            exportRepository.exportNotifications(
                notifications = notifications,
                targetUriString = "content://export",
                fileName = "file.csv"
            )
        }
    }
}
