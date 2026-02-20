package com.kuro.notiflow.domain.use_case

import com.kuro.notiflow.domain.api.notifications.NotificationRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ClearAllNotificationsUseCaseTest {

    private val repository = mockk<NotificationRepository>()
    private val useCase = ClearAllNotificationsUseCase(repository)

    @Test
    fun `invoke clears all notifications`() = runTest {
        coEvery { repository.clearAll() } returns Unit

        useCase()

        coVerify(exactly = 1) { repository.clearAll() }
    }
}
