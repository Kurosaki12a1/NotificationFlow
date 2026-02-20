package com.kuro.notiflow.domain.use_case

import com.kuro.notiflow.domain.api.settings.SettingsMenuRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ResetSettingsUseCaseTest {

    private val repository = mockk<SettingsMenuRepository>()
    private val useCase = ResetSettingsUseCase(repository)

    @Test
    fun `invoke resets settings`() = runTest {
        coEvery { repository.resetAllSettings() } returns Result.success(1)

        useCase()

        coVerify(exactly = 1) { repository.resetAllSettings() }
    }
}
