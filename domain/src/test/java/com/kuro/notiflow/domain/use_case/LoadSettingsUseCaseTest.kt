package com.kuro.notiflow.domain.use_case

import com.kuro.notiflow.domain.api.settings.SettingsMenuRepository
import com.kuro.notiflow.domain.models.settings.SettingsModel
import com.kuro.notiflow.domain.utils.SettingsFactory
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoadSettingsUseCaseTest {

    private val repository = mockk<SettingsMenuRepository>()
    private val useCase = LoadSettingsUseCase(repository)

    @Test
    fun `invoke emits settings when repository succeeds`() = runTest {
        val settings = SettingsFactory.model(isDynamicColorEnabled = true)
        coEvery { repository.fetchAllSettings() } returns flowOf(Result.success(settings))

        val result = useCase().first()

        assertEquals(settings, result)
    }
}
