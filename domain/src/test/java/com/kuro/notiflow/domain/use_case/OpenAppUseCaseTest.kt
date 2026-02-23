package com.kuro.notiflow.domain.use_case

import com.kuro.notiflow.domain.api.app.AppLauncher
import com.kuro.notiflow.domain.models.app.AppLaunchResult
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test

class OpenAppUseCaseTest {

    private val appLauncher = mockk<AppLauncher>()
    private val useCase = OpenAppUseCase(appLauncher)

    @Test
    fun `invoke returns opened when launcher opens app`() {
        every { appLauncher.openApp("com.example.app") } returns AppLaunchResult.OPENED

        val result = useCase("com.example.app")

        assertEquals(AppLaunchResult.OPENED, result)
        verify(exactly = 1) { appLauncher.openApp("com.example.app") }
    }

    @Test
    fun `invoke returns opened app info when launcher opens app info`() {
        every { appLauncher.openApp("com.example.app") } returns AppLaunchResult.OPENED_APP_INFO

        val result = useCase("com.example.app")

        assertEquals(AppLaunchResult.OPENED_APP_INFO, result)
        verify(exactly = 1) { appLauncher.openApp("com.example.app") }
    }

    @Test
    fun `invoke returns failed when launcher fails`() {
        every { appLauncher.openApp("com.example.app") } returns AppLaunchResult.FAILED

        val result = useCase("com.example.app")

        assertEquals(AppLaunchResult.FAILED, result)
        verify(exactly = 1) { appLauncher.openApp("com.example.app") }
    }
}
