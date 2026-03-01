package com.kuro.notiflow.domain.use_case

import com.kuro.notiflow.domain.api.datastore.AppDataRepository
import com.kuro.notiflow.domain.models.notifications.NotificationFilterMode
import com.kuro.notiflow.domain.models.notifications.NotificationFilterSettings
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class LoadNotificationFilterSettingsUseCaseTest {

    private val repository = mockk<AppDataRepository>()
    private val useCase = LoadNotificationFilterSettingsUseCase(repository)

    @Test
    fun `invoke returns notification filter settings flow`() = runTest {
        val expected = NotificationFilterSettings(
            mode = NotificationFilterMode.BLOCK_LIST,
            packageNames = setOf("com.google.android.youtube")
        )
        every { repository.notificationFilterSettings } returns flowOf(expected)

        val result = useCase().first()

        assertEquals(expected, result)
    }
}
