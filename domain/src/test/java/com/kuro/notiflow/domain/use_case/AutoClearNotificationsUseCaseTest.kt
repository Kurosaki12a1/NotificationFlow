package com.kuro.notiflow.domain.use_case

import com.kuro.notiflow.domain.Constants
import com.kuro.notiflow.domain.api.notifications.NotificationRepository
import com.kuro.notiflow.domain.api.settings.SettingsMenuRepository
import com.kuro.notiflow.domain.models.settings.SettingsModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AutoClearNotificationsUseCaseTest {

    private val notificationRepository = mockk<NotificationRepository>(relaxed = true)
    private val settingsRepository = mockk<SettingsMenuRepository>()
    private val useCase = AutoClearNotificationsUseCase(
        notificationRepository = notificationRepository,
        settingsRepository = settingsRepository
    )

    @Test
    fun `invoke skips delete when retention is always`() = runTest {
        val settings = SettingsModel(dataRetentionDays = 0)
        coEvery { settingsRepository.fetchAllSettings() } returns flowOf(Result.success(settings))

        useCase(currentTimeMillis = 10_000)

        coVerify(exactly = 0) { notificationRepository.deleteOlderThan(any()) }
    }

    @Test
    fun `invoke deletes when retention is custom`() = runTest {
        val settings = SettingsModel(dataRetentionDays = 3)
        coEvery { settingsRepository.fetchAllSettings() } returns flowOf(Result.success(settings))

        useCase(currentTimeMillis = 10_000)

        val expectedCutoff = 10_000 - 3L * Constants.Time.MILLIS_PER_DAY
        coVerify(exactly = 1) { notificationRepository.deleteOlderThan(expectedCutoff) }
    }

    @Test
    fun `invoke uses default when settings is default`() = runTest {
        coEvery { settingsRepository.fetchAllSettings() } returns flowOf(Result.success(SettingsModel()))

        useCase(currentTimeMillis = 100_000)

        val expectedCutoff =
            100_000 - Constants.Settings.DEFAULT_RETENTION_DAYS * Constants.Time.MILLIS_PER_DAY
        coVerify(exactly = 1) { notificationRepository.deleteOlderThan(expectedCutoff) }
    }
}
