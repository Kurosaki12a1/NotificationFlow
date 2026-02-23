package com.kuro.notiflow.domain.use_case

import com.kuro.notiflow.domain.api.notifications.NotificationRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class SetNotificationBookmarkUseCaseTest {

    private val repository = mockk<NotificationRepository>()
    private val useCase = SetNotificationBookmarkUseCase(repository)

    @Test
    fun `invoke updates bookmark`() = runTest {
        coEvery { repository.setBookmarked(42L, true) } returns Unit

        useCase(42L, true)

        coVerify(exactly = 1) { repository.setBookmarked(42L, true) }
    }
}
