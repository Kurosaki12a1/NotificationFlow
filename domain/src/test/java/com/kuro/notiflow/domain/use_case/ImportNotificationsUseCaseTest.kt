package com.kuro.notiflow.domain.use_case

import com.kuro.notiflow.domain.api.importer.NotificationImportRepository
import com.kuro.notiflow.domain.api.datastore.AppDataRepository
import com.kuro.notiflow.domain.api.notifications.NotificationRepository
import com.kuro.notiflow.domain.models.notifications.NotificationFilterMode
import com.kuro.notiflow.domain.models.notifications.NotificationFilterSettings
import com.kuro.notiflow.domain.models.notifications.NotificationModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ImportNotificationsUseCaseTest {

    private val notificationRepository = mockk<NotificationRepository>()
    private val importRepository = mockk<NotificationImportRepository>()
    private val appDataRepository = mockk<AppDataRepository>()

    private val useCase = ImportNotificationsUseCase(
        notificationRepository = notificationRepository,
        importRepository = importRepository,
        appDataRepository = appDataRepository
    )

    @Test
    fun `returns failure when import fails`() = runBlocking {
        val error = IllegalStateException("import failed")
        every {
            appDataRepository.notificationFilterSettings
        } returns flowOf(NotificationFilterSettings())
        coEvery { importRepository.importNotifications("content://test") } returns Result.failure(error)

        val result = useCase("content://test")

        assertTrue(result.isFailure)
        assertEquals(error, result.exceptionOrNull())
        coVerify(exactly = 0) { notificationRepository.addNotifications(any()) }
    }

    @Test
    fun `returns zero when imported list is empty`() = runBlocking {
        every {
            appDataRepository.notificationFilterSettings
        } returns flowOf(NotificationFilterSettings())
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
        every {
            appDataRepository.notificationFilterSettings
        } returns flowOf(NotificationFilterSettings())
        coEvery { importRepository.importNotifications("content://test") } returns Result.success(notifications)
        coEvery { notificationRepository.addNotifications(notifications) } returns Unit

        val result = useCase("content://test")

        assertTrue(result.isSuccess)
        assertEquals(notifications.size, result.getOrNull())
        coVerify(exactly = 1) { notificationRepository.addNotifications(notifications) }
    }

    @Test
    fun `skips blocked packages during import when option enabled`() = runBlocking {
        val notifications = listOf(
            NotificationModel(
                id = 1,
                packageName = "com.blocked",
                title = "Blocked",
                text = "Blocked",
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
            ),
            NotificationModel(
                id = 2,
                packageName = "com.allowed",
                title = "Allowed",
                text = "Allowed",
                subText = null,
                bigText = null,
                summaryText = null,
                infoText = null,
                textLines = null,
                postTime = 1_700_000_000_100,
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
        every {
            appDataRepository.notificationFilterSettings
        } returns flowOf(
            NotificationFilterSettings(
                mode = NotificationFilterMode.BLOCK_LIST,
                packageNames = setOf("com.blocked")
            )
        )
        coEvery { importRepository.importNotifications("content://test") } returns Result.success(notifications)
        coEvery { notificationRepository.addNotifications(listOf(notifications[1])) } returns Unit

        val result = useCase("content://test", skipBlockedPackages = true)

        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrNull())
        coVerify(exactly = 1) {
            notificationRepository.addNotifications(match {
                it.size == 1 && it.first().packageName == "com.allowed"
            })
        }
    }
}
