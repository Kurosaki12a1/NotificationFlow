package com.kuro.notiflow.data.impl

import com.kuro.notiflow.data.data_source.bookmark.BookmarkRuleLocalDataSource
import com.kuro.notiflow.data.data_source.entity.BookmarkRuleEntity
import com.kuro.notiflow.data.data_source.entity.NotificationEntity
import com.kuro.notiflow.data.data_source.notification.NotificationLocalDataSource
import com.kuro.notiflow.data.framework.app.AppInfoResolver
import com.kuro.notiflow.domain.models.bookmark.BookmarkRule
import com.kuro.notiflow.domain.models.bookmark.BookmarkRuleMatchField
import com.kuro.notiflow.domain.models.bookmark.BookmarkRuleMatchType
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BookmarkRuleRepositoryImplTest {

    private val ruleDataSource = mockk<BookmarkRuleLocalDataSource>()
    private val notificationDataSource = mockk<NotificationLocalDataSource>()
    private val appInfoResolver = mockk<AppInfoResolver>()
    private val repository = BookmarkRuleRepositoryImpl(
        ruleDataSource = ruleDataSource,
        notificationDataSource = notificationDataSource,
        appInfoResolver = appInfoResolver
    )

    @Test
    fun `fetchRules maps entities to domain models`() = runTest {
        every {
            ruleDataSource.fetchRules()
        } returns flowOf(
            listOf(
                ruleEntity(
                    id = 7L,
                    packageName = "pkg",
                    keyword = "otp",
                    matchField = BookmarkRuleMatchField.TEXT,
                    matchType = BookmarkRuleMatchType.CONTAINS
                )
            )
        )

        val result = repository.fetchRules().first()

        assertEquals(1, result.size)
        assertEquals(7L, result.first().id)
        assertEquals("pkg", result.first().packageName)
        assertEquals(BookmarkRuleMatchField.TEXT, result.first().matchField)
        assertEquals(BookmarkRuleMatchType.CONTAINS, result.first().matchType)
    }

    @Test
    fun `upsertRule rejects duplicate rule before persisting`() = runTest {
        coEvery { ruleDataSource.getAllRules() } returns listOf(
            ruleEntity(
                id = 10L,
                packageName = " pkg ",
                keyword = " OTP ",
                matchField = BookmarkRuleMatchField.TEXT,
                matchType = BookmarkRuleMatchType.CONTAINS
            )
        )

        val thrown = runCatching {
            repository.upsertRule(
                rule(
                    packageName = "pkg",
                    keyword = "otp",
                    matchField = BookmarkRuleMatchField.TEXT,
                    matchType = BookmarkRuleMatchType.CONTAINS
                )
            )
        }.exceptionOrNull()

        assertTrue(thrown is IllegalArgumentException)
        if (thrown != null) {
            assertEquals("Duplicate bookmark rule", thrown.message)
        }

        coVerify(exactly = 0) { ruleDataSource.upsertRule(any()) }
    }

    @Test
    fun `upsertRule backfills matching notifications when enabled`() = runTest {
        val existing = notificationEntity(
            id = 1L,
            packageName = "pkg.youtube",
            title = "Your OTP is ready",
            text = "Use OTP 123456",
            isBookmarked = false
        )
        val alreadyBookmarked = notificationEntity(
            id = 2L,
            packageName = "pkg.youtube",
            text = "OTP old",
            isBookmarked = true
        )
        val differentPackage = notificationEntity(
            id = 3L,
            packageName = "pkg.other",
            text = "OTP here",
            isBookmarked = false
        )

        coEvery { ruleDataSource.getAllRules() } returns emptyList()
        coEvery { ruleDataSource.upsertRule(any()) } returns 99L
        coEvery { notificationDataSource.getAllNotifications() } returns listOf(
            existing,
            alreadyBookmarked,
            differentPackage
        )
        coEvery { notificationDataSource.setBookmarked(any(), any()) } returns Unit

        val result = repository.upsertRule(
            rule(
                packageName = "pkg.youtube",
                keyword = "otp",
                matchField = BookmarkRuleMatchField.TITLE_OR_TEXT,
                matchType = BookmarkRuleMatchType.CONTAINS,
                isEnabled = true
            )
        )

        assertEquals(99L, result)
        coVerify(exactly = 1) { ruleDataSource.upsertRule(any()) }
        coVerify(exactly = 1) { notificationDataSource.setBookmarked(1L, true) }
        coVerify(exactly = 0) { notificationDataSource.setBookmarked(2L, true) }
        coVerify(exactly = 0) { notificationDataSource.setBookmarked(3L, true) }
    }

    @Test
    fun `upsertRule does not backfill when rule is disabled`() = runTest {
        coEvery { ruleDataSource.getAllRules() } returns emptyList()
        coEvery { ruleDataSource.upsertRule(any()) } returns 12L

        repository.upsertRule(
            rule(
                keyword = "otp",
                isEnabled = false
            )
        )

        coVerify(exactly = 1) { ruleDataSource.upsertRule(any()) }
        coVerify(exactly = 0) { notificationDataSource.getAllNotifications() }
        coVerify(exactly = 0) { notificationDataSource.setBookmarked(any(), any()) }
    }

    @Test
    fun `fetchAvailableApps resolves labels and sorts by app name`() = runTest {
        coEvery { notificationDataSource.getDistinctPackageNames() } returns listOf(
            "pkg.beta",
            "pkg.alpha"
        )
        every { appInfoResolver.resolveAppName("pkg.beta") } returns "Zeta"
        every { appInfoResolver.resolveAppName("pkg.alpha") } returns "Alpha"

        val result = repository.fetchAvailableApps()

        assertEquals(2, result.size)
        assertEquals("pkg.alpha", result[0].packageName)
        assertEquals("Alpha", result[0].appName)
        assertEquals("pkg.beta", result[1].packageName)
        assertEquals("Zeta", result[1].appName)
    }

    @Test
    fun `deleteRule delegates to data source`() = runTest {
        coEvery { ruleDataSource.deleteRule(5L) } returns Unit

        repository.deleteRule(5L)

        coVerify(exactly = 1) { ruleDataSource.deleteRule(5L) }
    }

    private fun rule(
        packageName: String? = null,
        keyword: String,
        matchField: BookmarkRuleMatchField = BookmarkRuleMatchField.TITLE_OR_TEXT,
        matchType: BookmarkRuleMatchType = BookmarkRuleMatchType.CONTAINS,
        isEnabled: Boolean = true
    ) = BookmarkRule(
        packageName = packageName,
        keyword = keyword,
        matchField = matchField,
        matchType = matchType,
        isEnabled = isEnabled
    )

    private fun ruleEntity(
        id: Long = 0L,
        packageName: String? = null,
        keyword: String,
        matchField: BookmarkRuleMatchField = BookmarkRuleMatchField.TITLE_OR_TEXT,
        matchType: BookmarkRuleMatchType = BookmarkRuleMatchType.CONTAINS,
        isEnabled: Boolean = true
    ) = BookmarkRuleEntity(
        id = id,
        packageName = packageName,
        keyword = keyword,
        matchField = matchField.name,
        matchType = matchType.name,
        isEnabled = isEnabled
    )

    private fun notificationEntity(
        id: Long = 0L,
        packageName: String = "pkg",
        title: String? = null,
        text: String? = null,
        isBookmarked: Boolean = false
    ) = NotificationEntity(
        id = id,
        packageName = packageName,
        title = title,
        text = text,
        subText = null,
        bigText = null,
        summaryText = null,
        infoText = null,
        textLines = null,
        postTime = 1_000L,
        priority = 0,
        category = "test",
        smallIconResId = null,
        iconBase64 = null,
        groupKey = null,
        channelId = null,
        isRead = false,
        isBookmarked = isBookmarked
    )
}
