package com.kuro.notiflow.data.impl

import com.kuro.notiflow.domain.Constants
import com.kuro.notiflow.domain.utils.TimeProvider
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

/**
 * An implementation of [TimeProvider] that uses the standard Java `java.time` library
 * and the system's default time zone.
 *
 * This class provides a concrete, testable way to access specific points in time
 * (like the start of a day or week) without hardcoding `System.currentTimeMillis()`
 * or `LocalDate.now()` throughout the application. It can be easily replaced with a
 * mock implementation in tests.
 *
 * @property zone The time zone used for all date and time calculations. Defaults to the
 *                system's current default time zone.
 */
class SystemTimeProvider @Inject constructor(
    override val zone: ZoneId = ZoneId.systemDefault()
) : TimeProvider {

    /**
     * Returns the timestamp for the beginning of the current day (00:00:00.000).
     *
     * @return The start of the current day in epoch milliseconds.
     */
    override fun startOfDay(): Long {
        val now = LocalDate.now(zone)
        return now.atStartOfDay(zone).toInstant().toEpochMilli()
    }

    /**
     * Returns the timestamp for the very end of the current day (23:59:59.999).
     *
     * This is calculated by finding the start of the *next* day and subtracting a small
     * offset, which is a robust way to avoid timezone and daylight saving issues.
     *
     * @return The end of the current day in epoch milliseconds.
     */
    override fun endOfDay(): Long {
        val now = LocalDate.now(zone)
        return now.plusDays(1).atStartOfDay(zone).toInstant().toEpochMilli() -
                Constants.Time.END_OF_DAY_OFFSET_MILLIS
    }

    /**
     * Returns the timestamp for the start of the current week, defined as Monday.
     *
     * It finds the most recent Monday, including today if today is Monday.
     *
     * @return The start of the most recent Monday in epoch milliseconds.
     */
    override fun startOfThisWeek(): Long {
        val now = LocalDate.now(zone)
        val monday = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        return monday.atStartOfDay(zone).toInstant().toEpochMilli()
    }

    /**
     * Returns the timestamp for the start of the previous week (last Monday).
     *
     * @return The start of last week's Monday in epoch milliseconds.
     */
    override fun startOfLastWeek(): Long {
        val now = LocalDate.now(zone)
        val mondayThisWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        val mondayLastWeek = mondayThisWeek.minusWeeks(1)
        return mondayLastWeek.atStartOfDay(zone).toInstant().toEpochMilli()
    }

    /**
     * Returns the timestamp for the end of the previous week (last Sunday at 23:59:59.999).
     *
     * This is calculated by finding the start of the current week (Monday) and subtracting
     * a small offset, effectively landing on the last millisecond of the previous Sunday.
     *
     * @return The end of the previous week in epoch milliseconds.
     */
    override fun endOfLastWeek(): Long {
        val now = LocalDate.now(zone)
        val mondayThisWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        // The start of this week's Monday minus an offset gives the end of last week's Sunday.
        return mondayThisWeek.atStartOfDay(zone).toInstant().toEpochMilli() -
                Constants.Time.END_OF_DAY_OFFSET_MILLIS
    }
}