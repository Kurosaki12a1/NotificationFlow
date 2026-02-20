package com.kuro.notiflow.data.impl

import com.kuro.notiflow.data.data_source.settings.SettingsLocalDataSource
import com.kuro.notiflow.data.utils.SettingsFactory
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsMenuRepositoryImplTest {

    private val dataSource = mockk<SettingsLocalDataSource>()
    private val repository = SettingsMenuRepositoryImpl(dataSource)

    @Test
    fun `updateSettings delegates to data source`() = runTest {
        val settings = SettingsFactory.model(
            language = com.kuro.notiflow.domain.models.settings.LanguageType.EN,
            themeType = com.kuro.notiflow.domain.models.settings.ThemeType.DARK,
            colorsType = com.kuro.notiflow.domain.models.settings.ColorType.RED,
            isDynamicColorEnabled = true,
            secureMode = true
        )
        coEvery { dataSource.updateSettings(any()) } returns 1

        val result = repository.updateSettings(settings)

        assertEquals(true, result.isSuccess)
        coVerify(exactly = 1) { dataSource.updateSettings(match { it.id == 1 }) }
    }

    @Test
    fun `fetchAllSettings maps to domain`() = runTest {
        val entity = SettingsFactory.entity(
            id = 1,
            language = com.kuro.notiflow.domain.models.settings.LanguageType.EN,
            themeType = com.kuro.notiflow.domain.models.settings.ThemeType.DARK,
            colorsType = com.kuro.notiflow.domain.models.settings.ColorType.RED,
            isDynamicColorEnabled = true,
            secureMode = true,
            dataRetentionDays = 90
        )
        every { dataSource.fetchSettingsFlow() } returns flowOf(entity)

        val result = repository.fetchAllSettings().first()

        assertEquals(true, result.isSuccess)
        assertEquals(true, result.getOrNull()?.isDynamicColorEnabled)
    }

    @Test
    fun `resetAllSettings writes default settings`() = runTest {
        coEvery { dataSource.updateSettings(any()) } returns 1

        val result = repository.resetAllSettings()

        assertEquals(true, result.isSuccess)
        coVerify(exactly = 1) { dataSource.updateSettings(match { it.id == 1 }) }
    }
}
