package com.kuro.notiflow.domain.use_case

import com.kuro.notiflow.domain.api.importer.NotificationImportRepository
import com.kuro.notiflow.domain.api.notifications.NotificationRepository
import com.kuro.notiflow.domain.models.notifications.NotificationModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ImportNotificationsUseCaseTest {

    private val notificationRepository = mockk<NotificationRepository>()
    private val importRepository = mockk<NotificationImportRepository>()

    private val useCase = ImportNotificationsUseCase(
        notificationRepository = notificationRepository,
        importRepository = importRepository
    )

    @Test
    fun `returns failure when import fails`() = runBlocking {
        val error = IllegalStateException("import failed")
        coEvery { importRepository.importNotifications("content://test") } returns Result.failure(error)

        val result = useCase("content://test")

        assertTrue(result.isFailure)
        assertEquals(error, result.exceptionOrNull())
        coVerify(exactly = 0) { notificationRepository.addNotifications(any()) }
    }

    @Test
    fun `returns zero when imported list is empty`() = runBlocking {
        coEvery { importRepository.importNotifications("content://test") } returns Result.success(emptyList())

        val result = useCase("content://test")

        assertTrue(result.isSuccess)
        assertEquals(0, result.getOrNull())
        coVerify(exactly = 0) { notificationRepository.addNotifications(any()) }
    }

    @Test
    fun `adds notifications when import succeeds`() = runBlocking {
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
        coEvery { importRepository.importNotifications("content://test") } returns Result.success(notifications)
        coEvery { notificationRepository.addNotifications(notifications) } returns Unit

        val result = useCase("content://test")

        assertTrue(result.isSuccess)
        assertEquals(notifications.size, result.getOrNull())
        coVerify(exactly = 1) { notificationRepository.addNotifications(notifications) }
    }
}
