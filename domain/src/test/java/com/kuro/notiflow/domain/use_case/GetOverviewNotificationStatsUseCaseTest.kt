package com.kuro.notiflow.domain.use_case

import com.kuro.notiflow.domain.api.notifications.NotificationRepository
import com.kuro.notiflow.domain.models.notifications.NotificationStats
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetOverviewNotificationStatsUseCaseTest {

    private val repository = mockk<NotificationRepository>()
    private val useCase = GetOverviewNotificationStatsUseCase(repository)

    @Test
    fun `invoke emits stats when repository succeeds`() = runTest {
        val stats = NotificationStats(totalCount = 5)
        every { repository.getNotificationsStats() } returns flowOf(Result.success(stats))

        val result = useCase().first()

        assertEquals(stats, result)
    }

    @Test
    fun `invoke emits default stats when repository fails`() = runTest {
        every { repository.getNotificationsStats() } returns flowOf(Result.failure(Exception("boom")))

        val result = useCase().first()

        assertEquals(NotificationStats(), result)
    }
}
