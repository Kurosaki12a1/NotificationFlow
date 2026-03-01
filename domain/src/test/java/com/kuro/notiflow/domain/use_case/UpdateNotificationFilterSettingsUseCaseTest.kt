package com.kuro.notiflow.domain.use_case

import com.kuro.notiflow.domain.api.datastore.AppDataRepository
import com.kuro.notiflow.domain.models.notifications.NotificationFilterMode
import com.kuro.notiflow.domain.models.notifications.NotificationFilterSettings
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class UpdateNotificationFilterSettingsUseCaseTest {

    private val repository = mockk<AppDataRepository>()
    private val useCase = UpdateNotificationFilterSettingsUseCase(repository)

    @Test
    fun `invoke updates notification filter settings`() = runTest {
        val settings = NotificationFilterSettings(
            mode = NotificationFilterMode.ALLOW_LIST,
            packageNames = setOf("com.google.android.gm")
        )
        coEvery { repository.updateNotificationFilterSettings(settings) } returns Unit

        useCase(settings)

        coVerify(exactly = 1) { repository.updateNotificationFilterSettings(settings) }
    }
}
