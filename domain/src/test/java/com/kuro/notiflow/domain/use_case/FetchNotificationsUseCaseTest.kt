package com.kuro.notiflow.domain.use_case

import androidx.paging.PagingData
import com.kuro.notiflow.domain.api.notifications.NotificationRepository
import com.kuro.notiflow.domain.models.notifications.NotificationModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertSame
import org.junit.Test

class FetchNotificationsUseCaseTest {

    private val repository = mockk<NotificationRepository>()
    private val useCase = FetchNotificationsUseCase(repository)

    @Test
    fun `invoke returns repository flow`() {
        val flow = flowOf(PagingData.empty<NotificationModel>())
        every { repository.fetchAllNotifications() } returns flow

        val result = useCase()

        assertSame(flow, result)
    }
}
