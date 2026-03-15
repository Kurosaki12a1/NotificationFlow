package com.kuro.notiflow.presentation.settings.ui.notification_filters

import com.kuro.notiflow.domain.models.app.AppSelectionItem
import com.kuro.notiflow.domain.models.notifications.NotificationFilterMode
import com.kuro.notiflow.domain.models.notifications.NotificationFilterSettings
import com.kuro.notiflow.domain.use_case.FetchInstalledAppsUseCase
import com.kuro.notiflow.domain.use_case.LoadNotificationFilterSettingsUseCase
import com.kuro.notiflow.domain.use_case.UpdateNotificationFilterSettingsUseCase
import com.kuro.notiflow.presentation.settings.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class NotificationFiltersViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    @OptIn(ExperimentalCoroutinesApi::class)
    fun `keeps loading until installed apps are loaded`() = runTest {
        val settingsFlow = MutableSharedFlow<NotificationFilterSettings>(replay = 1)
        settingsFlow.tryEmit(NotificationFilterSettings(mode = NotificationFilterMode.BLOCK_LIST))
        val loadSettingsUseCase = mockk<LoadNotificationFilterSettingsUseCase>()
        every { loadSettingsUseCase.invoke() } returns settingsFlow

        val installedAppsGate = CompletableDeferred<List<AppSelectionItem>>()
        val fetchInstalledAppsUseCase = mockk<FetchInstalledAppsUseCase>()
        coEvery { fetchInstalledAppsUseCase.invoke() } coAnswers { installedAppsGate.await() }

        val viewModel = createViewModel(
            fetchInstalledAppsUseCase = fetchInstalledAppsUseCase,
            loadSettingsUseCase = loadSettingsUseCase
        )

        awaitUntil { viewModel.state.value.mode == NotificationFilterMode.BLOCK_LIST }
        assertTrue(viewModel.state.value.isLoading)
        assertEquals(NotificationFilterMode.BLOCK_LIST, viewModel.state.value.mode)

        installedAppsGate.complete(
            listOf(AppSelectionItem(packageName = "com.test.app", appName = "Test App"))
        )

        awaitUntil { !viewModel.state.value.isLoading }
        assertFalse(viewModel.state.value.isLoading)
        assertEquals(1, viewModel.state.value.apps.size)
    }

    @Test
    @OptIn(ExperimentalCoroutinesApi::class)
    fun `shows empty state after installed apps load with no results`() = runTest {
        val loadSettingsUseCase = mockk<LoadNotificationFilterSettingsUseCase>()
        every { loadSettingsUseCase.invoke() } returns flowOf(NotificationFilterSettings())
        val fetchInstalledAppsUseCase = mockk<FetchInstalledAppsUseCase>()
        coEvery { fetchInstalledAppsUseCase.invoke() } returns emptyList()

        val viewModel = createViewModel(
            fetchInstalledAppsUseCase = fetchInstalledAppsUseCase,
            loadSettingsUseCase = loadSettingsUseCase
        )

        awaitUntil { !viewModel.state.value.isLoading }

        assertFalse(viewModel.state.value.isLoading)
        assertTrue(viewModel.state.value.apps.isEmpty())
        assertEquals(NotificationFilterMode.ALLOW_ALL, viewModel.state.value.mode)
    }

    private suspend fun awaitUntil(condition: () -> Boolean) {
        withTimeout(1_000) {
            while (!condition()) {
                delay(10)
            }
        }
    }

    private fun createViewModel(
        fetchInstalledAppsUseCase: FetchInstalledAppsUseCase = mockk(),
        loadSettingsUseCase: LoadNotificationFilterSettingsUseCase = mockk(),
        updateNotificationFilterSettingsUseCase: UpdateNotificationFilterSettingsUseCase = mockk(relaxed = true)
    ): NotificationFiltersViewModel {
        return NotificationFiltersViewModel(
            fetchInstalledAppsUseCase = fetchInstalledAppsUseCase,
            loadNotificationFilterSettingsUseCase = loadSettingsUseCase,
            updateNotificationFilterSettingsUseCase = updateNotificationFilterSettingsUseCase
        )
    }
}
