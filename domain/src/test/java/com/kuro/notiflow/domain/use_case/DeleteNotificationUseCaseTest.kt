package com.kuro.notiflow.domain.use_case

import com.kuro.notiflow.domain.api.notifications.NotificationRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class DeleteNotificationUseCaseTest {

    private val repository = mockk<NotificationRepository>()
    private val useCase = DeleteNotificationUseCase(repository)

    @Test
    fun `invoke deletes notification by id`() = runTest {
        coEvery { repository.deleteNotificationById(42L) } returns Unit

        useCase(42L)

        coVerify(exactly = 1) { repository.deleteNotificationById(42L) }
    }
}
