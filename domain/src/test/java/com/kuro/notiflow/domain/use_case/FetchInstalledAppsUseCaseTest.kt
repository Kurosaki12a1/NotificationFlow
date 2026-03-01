package com.kuro.notiflow.domain.use_case

import com.kuro.notiflow.domain.api.app.AppInfoProvider
import com.kuro.notiflow.domain.models.app.AppSelectionItem
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class FetchInstalledAppsUseCaseTest {

    private val appInfoProvider = mockk<AppInfoProvider>()
    private val useCase = FetchInstalledAppsUseCase(appInfoProvider)

    @Test
    fun `invoke returns installed apps from provider`() = runTest {
        val expected = listOf(
            AppSelectionItem(
                packageName = "com.google.android.youtube",
                appName = "YouTube"
            ),
            AppSelectionItem(
                packageName = "com.google.android.gm",
                appName = "Gmail"
            )
        )
        coEvery { appInfoProvider.fetchInstalledApps() } returns expected

        val result = useCase()

        assertEquals(expected, result)
        coVerify(exactly = 1) { appInfoProvider.fetchInstalledApps() }
    }
}
