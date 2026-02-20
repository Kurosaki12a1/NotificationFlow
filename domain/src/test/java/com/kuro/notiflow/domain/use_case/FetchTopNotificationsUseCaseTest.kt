package com.kuro.notiflow.domain.use_case

import com.kuro.notiflow.domain.api.notifications.NotificationRepository
import com.kuro.notiflow.domain.models.notifications.PackageStats
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertSame
import org.junit.Test

class FetchTopNotificationsUseCaseTest {

    private val repository = mockk<NotificationRepository>()
    private val useCase = FetchTopNotificationsUseCase(repository)

    @Test
    fun `invoke returns repository flow`() {
        val flow = flowOf(listOf<PackageStats>())
        every { repository.fetchTopRecentNotifications() } returns flow

        val result = useCase()

        assertSame(flow, result)
    }
}
