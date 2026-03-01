package com.kuro.notiflow.data.impl

import com.kuro.notiflow.data.data_source.datastore.AppDataStoreDataSource
import androidx.datastore.preferences.core.Preferences
import com.kuro.notiflow.domain.models.notifications.NotificationFilterMode
import com.kuro.notiflow.domain.models.notifications.NotificationFilterSettings
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

    @Test
    fun `combines notification filter settings from data source`() {
        val dataSource = mockk<AppDataStoreDataSource>()
        every {
            dataSource.get(
                any<Preferences.Key<Boolean>>(),
                any<Boolean>()
            )
        } returns flowOf(true)
        every {
            dataSource.get(
                any<Preferences.Key<String>>(),
                any<String>()
            )
        } returns flowOf(NotificationFilterMode.BLOCK_LIST.name)
        every {
            dataSource.get(
                any<Preferences.Key<Set<String>>>(),
                any<Set<String>>()
            )
        } returns flowOf(setOf("pkg.blocked"))
        val repository = AppDataRepositoryImpl(dataSource)

        val value = runBlocking { repository.notificationFilterSettings.first() }

        assertEquals(
            NotificationFilterSettings(
                mode = NotificationFilterMode.BLOCK_LIST,
                packageNames = setOf("pkg.blocked")
            ),
            value
        )
    }

    @Test
    fun `defaults notification filter mode when persisted value is invalid`() {
        val dataSource = mockk<AppDataStoreDataSource>()
        every {
            dataSource.get(
                any<Preferences.Key<Boolean>>(),
                any<Boolean>()
            )
        } returns flowOf(true)
        every {
            dataSource.get(
                any<Preferences.Key<String>>(),
                any<String>()
            )
        } returns flowOf("INVALID_MODE")
        every {
            dataSource.get(
                any<Preferences.Key<Set<String>>>(),
                any<Set<String>>()
            )
        } returns flowOf(setOf("pkg.allowed"))
        val repository = AppDataRepositoryImpl(dataSource)

        val value = runBlocking { repository.notificationFilterSettings.first() }

        assertEquals(
            NotificationFilterSettings(
                mode = NotificationFilterMode.ALLOW_ALL,
                packageNames = setOf("pkg.allowed")
            ),
            value
        )
    }

    @Test
    fun `delegates updateNotificationFilterSettings to data source`() = runBlocking {
        val dataSource = mockk<AppDataStoreDataSource>(relaxed = true)
        val repository = AppDataRepositoryImpl(dataSource)
        val settings = NotificationFilterSettings(
            mode = NotificationFilterMode.ALLOW_LIST,
            packageNames = setOf("pkg.allowed")
        )

        repository.updateNotificationFilterSettings(settings)

        coVerify(exactly = 1) { dataSource.set(any<Preferences.Key<String>>(), settings.mode.name) }
        coVerify(exactly = 1) {
            dataSource.set(
                any<Preferences.Key<Set<String>>>(),
                settings.packageNames
            )
        }
    }
}
