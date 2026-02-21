package com.kuro.notiflow.data.data_source.settings

import com.kuro.notiflow.data.entity.SettingsEntity
import com.kuro.notiflow.data.utils.SettingsFactory
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsLocalDataSourceImplTest {

    private val dao = mockk<SettingsDao>()
    private val dataSource = SettingsLocalDataSourceImpl(dao)

    @Test
    fun fetchSettingsFlow_delegates_to_dao() = runTest {
        val entity: SettingsEntity = SettingsFactory.entity()
        every { dao.fetchSettingsFlow() } returns flowOf(entity)

        val result = dataSource.fetchSettingsFlow()

        coVerify(exactly = 1) { dao.fetchSettingsFlow() }
        assertEquals(entity, result.first())
    }

    @Test
    fun updateSettings_delegates_to_dao() = runTest {
        val entity: SettingsEntity = SettingsFactory.entity()
        coEvery { dao.updateSettings(entity) } returns 1

        val result = dataSource.updateSettings(entity)

        assertEquals(1, result)
        coVerify(exactly = 1) { dao.updateSettings(entity) }
    }
}
