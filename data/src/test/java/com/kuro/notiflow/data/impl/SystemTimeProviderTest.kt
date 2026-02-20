package com.kuro.notiflow.data.impl

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters

class SystemTimeProviderTest {

    @Test
    fun `day boundaries are computed correctly`() {
        val zone = ZoneId.of("UTC")
        val provider = SystemTimeProvider(zone)

        val now = LocalDate.now(zone)
        val expectedStart = now.atStartOfDay(zone).toInstant().toEpochMilli()
        val expectedEnd = now.plusDays(1).atStartOfDay(zone).toInstant().toEpochMilli() - 1

        assertEquals(expectedStart, provider.startOfDay())
        assertEquals(expectedEnd, provider.endOfDay())
    }

    @Test
    fun `week boundaries are computed correctly`() {
        val zone = ZoneId.of("UTC")
        val provider = SystemTimeProvider(zone)

        val now = LocalDate.now(zone)
        val mondayThisWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        val mondayLastWeek = mondayThisWeek.minusWeeks(1)

        val expectedStartThisWeek = mondayThisWeek.atStartOfDay(zone).toInstant().toEpochMilli()
        val expectedStartLastWeek = mondayLastWeek.atStartOfDay(zone).toInstant().toEpochMilli()
        val expectedEndLastWeek = mondayThisWeek.atStartOfDay(zone).toInstant().toEpochMilli() - 1

        assertEquals(expectedStartThisWeek, provider.startOfThisWeek())
        assertEquals(expectedStartLastWeek, provider.startOfLastWeek())
        assertEquals(expectedEndLastWeek, provider.endOfLastWeek())
    }
}
