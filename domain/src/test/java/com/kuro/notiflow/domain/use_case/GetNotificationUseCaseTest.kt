package com.kuro.notiflow.domain.use_case

import com.kuro.notiflow.domain.api.notifications.NotificationRepository
import com.kuro.notiflow.domain.utils.NotificationFactory
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetNotificationUseCaseTest {

    private val repository = mockk<NotificationRepository>()
    private val useCase = GetNotificationUseCase(repository)

    @Test
    fun `invoke returns notification when repository succeeds`() = runTest {
        val expected = NotificationFactory.model(
            id = 1,
            title = "t",
            text = "x",
            postTime = 100
        )
        coEvery { repository.getNotificationById(1) } returns Result.success(expected)

        val result = useCase(1)

        assertEquals(expected, result)
    }

    @Test
    fun `invoke returns null when repository fails`() = runTest {
        coEvery { repository.getNotificationById(1) } returns Result.failure(Exception("fail"))

        val result = useCase(1)

        assertNull(result)
    }
}
