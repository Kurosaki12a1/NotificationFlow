package com.kuro.notiflow.domain.use_case

import com.kuro.notiflow.domain.api.notifications.NotificationRepository
import com.kuro.notiflow.domain.utils.NotificationFactory
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class AddNotificationUseCaseTest {

    private val repository = mockk<NotificationRepository>()
    private val useCase = AddNotificationUseCase(repository)

    @Test
    fun `invoke adds notification`() = runTest {
        val model = NotificationFactory.model()
        coEvery { repository.addNotification(model) } returns Unit

        useCase(model)

        coVerify(exactly = 1) { repository.addNotification(model) }
    }
}
