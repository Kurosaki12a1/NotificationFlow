package com.kuro.notiflow.data.impl

import androidx.paging.PagingData
import com.kuro.notiflow.data.data_source.notification.NotificationLocalDataSource
import com.kuro.notiflow.data.utils.NotificationFactory
import com.kuro.notiflow.domain.models.notifications.NotificationModel
import com.kuro.notiflow.domain.models.notifications.NotificationStats
import com.kuro.notiflow.domain.models.notifications.PackageStats
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NotificationRepositoryImplTest {

    private val dataSource = mockk<NotificationLocalDataSource>()
    private val repository = NotificationRepositoryImpl(dataSource)

    @Test
    fun `addNotification inserts when no recent notification`() = runTest {
        val model = NotificationFactory.model(postTime = 1_000)
        coEvery { dataSource.getRecentNotificationByPackage(model.packageName, model.postTime) } returns null
        coEvery { dataSource.addNotification(any()) } returns Unit

        repository.addNotification(model)

        coVerify(exactly = 1) { dataSource.addNotification(match { it.postTime == model.postTime }) }
    }

    @Test
    fun `addNotification inserts when text differs`() = runTest {
        val model = NotificationFactory.model(text = "new", postTime = 1_000)
        val recent = NotificationFactory.entity(text = "old", postTime = 900)
        coEvery { dataSource.getRecentNotificationByPackage(model.packageName, model.postTime) } returns recent
        coEvery { dataSource.addNotification(any()) } returns Unit

        repository.addNotification(model)

        coVerify(exactly = 1) { dataSource.addNotification(any()) }
    }

    @Test
    fun `addNotification skips when same text within interval`() = runTest {
        val model = NotificationFactory.model(text = "same", postTime = 1_000)
        val recent = NotificationFactory.entity(text = "same", postTime = 970)
        coEvery { dataSource.getRecentNotificationByPackage(model.packageName, model.postTime) } returns recent

        repository.addNotification(model)

        coVerify(exactly = 0) { dataSource.addNotification(any()) }
    }

    @Test
    fun `addNotification inserts when same text after interval`() = runTest {
        val model = NotificationFactory.model(text = "same", postTime = 100_000)
        val recent = NotificationFactory.entity(text = "same", postTime = 30_000)
        coEvery { dataSource.getRecentNotificationByPackage(model.packageName, model.postTime) } returns recent
        coEvery { dataSource.addNotification(any()) } returns Unit

        repository.addNotification(model)

        coVerify(exactly = 1) { dataSource.addNotification(any()) }
    }

    @Test
    fun `addNotifications maps and inserts list`() = runTest {
        val models = listOf(NotificationFactory.model(postTime = 1_000))
        coEvery { dataSource.addNotifications(any()) } returns Unit

        repository.addNotifications(models)

        coVerify(exactly = 1) { dataSource.addNotifications(match { it.size == 1 }) }
    }

    @Test
    fun `getNotificationById returns mapped result`() = runTest {
        val entity = NotificationFactory.entity(id = 10, postTime = 1_000)
        coEvery { dataSource.getNotificationById(10) } returns entity

        val result = repository.getNotificationById(10)

        assertEquals(true, result.isSuccess)
        assertEquals(10L, result.getOrNull()?.id)
    }

    @Test
    fun `getAllNotifications returns mapped list`() = runTest {
        val entities = listOf(NotificationFactory.entity(id = 1, postTime = 1_000))
        coEvery { dataSource.getAllNotifications() } returns entities

        val result = repository.getAllNotifications()

        assertEquals(1, result.getOrNull()?.size)
        assertEquals(1L, result.getOrNull()?.first()?.id)
    }

    @Test
    fun `fetchAllNotifications delegates to data source`() = runTest {
        every { dataSource.fetchAllNotifications() } returns flowOf(PagingData.empty())

        repository.fetchAllNotifications().first()

        verify(exactly = 1) { dataSource.fetchAllNotifications() }
    }

    @Test
    fun `fetchTopRecentNotifications delegates to data source`() = runTest {
        val stats = listOf(PackageStats("pkg", 1, 10.0))
        every { dataSource.fetchTopRecentNotifications() } returns flowOf(stats)

        val result = repository.fetchTopRecentNotifications().first()

        assertEquals(1, result.size)
    }

    @Test
    fun `getNotificationsStats wraps result`() = runTest {
        val stats = NotificationStats(totalCount = 5)
        every { dataSource.getNotificationsStats() } returns flowOf(stats)

        val result = repository.getNotificationsStats().first()

        assertEquals(true, result.isSuccess)
        assertEquals(5, result.getOrNull()?.totalCount)
    }

    @Test
    fun `getNotificationsByPackage returns mapped list`() = runTest {
        val entities = listOf(NotificationFactory.entity(id = 2, postTime = 1_000))
        coEvery { dataSource.getNotificationsByPackage("pkg") } returns entities

        val result = repository.getNotificationsByPackage("pkg")

        assertEquals(1, result.getOrNull()?.size)
        assertEquals(2L, result.getOrNull()?.first()?.id)
    }

    @Test
    fun `deleteNotification maps and delegates`() = runTest {
        val model = NotificationFactory.model(id = 3, postTime = 1_000)
        coEvery { dataSource.deleteNotification(any()) } returns Unit

        repository.deleteNotification(model)

        coVerify(exactly = 1) { dataSource.deleteNotification(match { it.postTime == model.postTime }) }
    }

    @Test
    fun `deleteNotificationById delegates`() = runTest {
        coEvery { dataSource.deleteNotificationById(5) } returns Unit

        repository.deleteNotificationById(5)

        coVerify(exactly = 1) { dataSource.deleteNotificationById(5) }
    }

    @Test
    fun `clearAll delegates`() = runTest {
        coEvery { dataSource.clearAll() } returns Unit

        repository.clearAll()

        coVerify(exactly = 1) { dataSource.clearAll() }
    }

}
