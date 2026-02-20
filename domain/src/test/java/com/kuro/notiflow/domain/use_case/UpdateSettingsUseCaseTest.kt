package com.kuro.notiflow.domain.use_case

import com.kuro.notiflow.domain.api.settings.SettingsMenuRepository
import com.kuro.notiflow.domain.utils.SettingsFactory
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UpdateSettingsUseCaseTest {

    private val repository = mockk<SettingsMenuRepository>()
    private val useCase = UpdateSettingsUseCase(repository)

    @Test
    fun `invoke updates settings`() = runTest {
        val settings = SettingsFactory.model(isDynamicColorEnabled = true)
        coEvery { repository.updateSettings(settings) } returns Result.success(1)

        useCase(settings)

        coVerify(exactly = 1) { repository.updateSettings(settings) }
    }
}
