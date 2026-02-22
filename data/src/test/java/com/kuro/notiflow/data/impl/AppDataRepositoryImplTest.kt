package com.kuro.notiflow.data.impl

import com.kuro.notiflow.data.data_source.data_store.AppDataStoreDataSource
import androidx.datastore.preferences.core.Preferences
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class AppDataRepositoryImplTest {

    @Test
    fun `delegates isFirstLaunch to data source`() {
        val dataSource = mockk<AppDataStoreDataSource>()
        every {
            dataSource.get(
                any<Preferences.Key<Boolean>>(),
                any<Boolean>()
            )
        } returns flowOf(true)
        val repository = AppDataRepositoryImpl(dataSource)

        val value = runBlocking { repository.isFirstLaunch.first() }

        assertEquals(true, value)
    }

    @Test
    fun `delegates setOnboardingCompleted to data source`() = runBlocking {
        val dataSource = mockk<AppDataStoreDataSource>(relaxed = true)
        val repository = AppDataRepositoryImpl(dataSource)

        repository.setOnboardingCompleted()

        coVerify(exactly = 1) { dataSource.set(any(), false) }
    }
}
