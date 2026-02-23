package com.kuro.notiflow.data.data_source.notification

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kuro.notiflow.data.entity.NotificationEntity
import com.kuro.notiflow.domain.models.notifications.NotificationStats
import com.kuro.notiflow.domain.models.notifications.PackageStats
import com.kuro.notiflow.domain.utils.TimeProvider
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
class NotificationLocalDataSourceImplTest {

    private val dao = mockk<NotificationDao>()
    private val timeProvider = mockk<TimeProvider>()
    private val dataSource = NotificationLocalDataSourceImpl(dao, timeProvider)

    @Test
    fun addNotification_delegates_to_dao() = runTest {
        val entity = fakeEntity()
        coEvery { dao.insert(entity) } returns Unit

        dataSource.addNotification(entity)

        coVerify(exactly = 1) { dao.insert(entity) }
    }

    @Test
    fun addNotifications_delegates_to_dao() = runTest {
        val entities = listOf(fakeEntity())
        coEvery { dao.insertAll(entities) } returns Unit

        dataSource.addNotifications(entities)

        coVerify(exactly = 1) { dao.insertAll(entities) }
    }

    @Test
    fun getNotificationById_delegates_to_dao() = runTest {
        val entity = fakeEntity(id = 10L)
        coEvery { dao.getById(10L) } returns entity

        val result = dataSource.getNotificationById(10L)

        assertEquals(entity, result)
        coVerify(exactly = 1) { dao.getById(10L) }
    }

    @Test
    fun getAllNotifications_delegates_to_dao() = runTest {
        val entities = listOf(fakeEntity())
        coEvery { dao.getAll() } returns entities

        val result = dataSource.getAllNotifications()

        assertEquals(entities, result)
        coVerify(exactly = 1) { dao.getAll() }
    }

    @Test
    fun fetchAllNotifications_creates_paging_source() = runTest {
        val pagingSource = object : PagingSource<Int, NotificationEntity>() {
            override fun getRefreshKey(
                state: PagingState<Int, NotificationEntity>
            ): Int? = null

            override suspend fun load(
                params: LoadParams<Int>
            ): LoadResult<Int, NotificationEntity> =
                LoadResult.Page(data = emptyList(), prevKey = null, nextKey = null)
        }
        every { dao.fetchAll() } returns pagingSource

        dataSource.fetchAllNotifications().first()

        verify(exactly = 1) { dao.fetchAll() }
    }

    @Test
    fun fetchBookmarkedNotifications_creates_paging_source() = runTest {
        val pagingSource = object : PagingSource<Int, NotificationEntity>() {
            override fun getRefreshKey(
                state: PagingState<Int, NotificationEntity>
            ): Int? = null

            override suspend fun load(
                params: LoadParams<Int>
            ): LoadResult<Int, NotificationEntity> =
                LoadResult.Page(data = emptyList(), prevKey = null, nextKey = null)
        }
        every { dao.fetchBookmarked() } returns pagingSource

        dataSource.fetchBookmarkedNotifications().first()

        verify(exactly = 1) { dao.fetchBookmarked() }
    }

    @Test
    fun fetchTopRecentNotifications_delegates_to_dao() = runTest {
        val stats = listOf(PackageStats(packageName = "pkg", count = 1, percentage = 100.0))
        every { dao.getTopPackages() } returns flowOf(stats)

        val result = dataSource.fetchTopRecentNotifications().first()

        assertEquals(stats, result)
        verify(exactly = 1) { dao.getTopPackages() }
    }

    @Test
    fun getNotificationsStats_uses_time_provider_ranges() = runTest {
        every { timeProvider.startOfDay() } returns 1L
        every { timeProvider.endOfDay() } returns 2L
        every { timeProvider.startOfLastWeek() } returns 3L
        every { timeProvider.endOfLastWeek() } returns 4L
        every { timeProvider.startOfThisWeek() } returns 5L

        val stats = NotificationStats()
        every { dao.getNotificationStats(1L, 2L, 3L, 4L, 5L) } returns flowOf(stats)

        val result = dataSource.getNotificationsStats().first()

        assertEquals(stats, result)
        verify(exactly = 1) { dao.getNotificationStats(1L, 2L, 3L, 4L, 5L) }
    }

    @Test
    fun getNotificationsByPackage_delegates_to_dao() = runTest {
        val entities = listOf(fakeEntity(packageName = "pkg"))
        coEvery { dao.getByPackage("pkg") } returns entities

        val result = dataSource.getNotificationsByPackage("pkg")

        assertEquals(entities, result)
        coVerify(exactly = 1) { dao.getByPackage("pkg") }
    }

    @Test
    fun getRecentNotificationByPackage_delegates_to_dao() = runTest {
        val entity = fakeEntity(packageName = "pkg")
        coEvery { dao.getRecentByPackage("pkg", 123L) } returns entity

        val result = dataSource.getRecentNotificationByPackage("pkg", 123L)

        assertEquals(entity, result)
        coVerify(exactly = 1) { dao.getRecentByPackage("pkg", 123L) }
    }

    @Test
    fun deleteNotification_delegates_to_dao() = runTest {
        val entity = fakeEntity()
        coEvery { dao.delete(entity) } returns Unit

        dataSource.deleteNotification(entity)

        coVerify(exactly = 1) { dao.delete(entity) }
    }

    @Test
    fun deleteNotificationById_delegates_to_dao() = runTest {
        coEvery { dao.deleteById(3L) } returns Unit

        dataSource.deleteNotificationById(3L)

        coVerify(exactly = 1) { dao.deleteById(3L) }
    }

    @Test
    fun deleteOlderThan_delegates_to_dao() = runTest {
        coEvery { dao.deleteOlderThan(100L) } returns Unit

        dataSource.deleteOlderThan(100L)

        coVerify(exactly = 1) { dao.deleteOlderThan(100L) }
    }

    @Test
    fun setBookmarked_delegates_to_dao() = runTest {
        coEvery { dao.updateBookmark(5L, true) } returns Unit

        dataSource.setBookmarked(5L, true)

        coVerify(exactly = 1) { dao.updateBookmark(5L, true) }
    }

    @Test
    fun clearAll_delegates_to_dao() = runTest {
        coEvery { dao.clearAll() } returns Unit

        dataSource.clearAll()

        coVerify(exactly = 1) { dao.clearAll() }
    }

    private fun fakeEntity(
        id: Long = 1L,
        packageName: String = "pkg"
    ) = NotificationEntity(
        id = id,
        packageName = packageName,
        title = "title",
        text = "text",
        subText = null,
        bigText = null,
        summaryText = null,
        infoText = null,
        textLines = null,
        postTime = 1_000L,
        priority = 0,
        category = "category",
        smallIconResId = null,
        iconBase64 = null,
        groupKey = null,
        channelId = null,
        isRead = false,
        isBookmarked = false
    )
}
