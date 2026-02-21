package com.kuro.notiflow.data.impl

import com.kuro.notiflow.domain.Constants
import com.kuro.notiflow.domain.utils.TimeProvider
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

class SystemTimeProvider @Inject constructor(
    override val zone: ZoneId = ZoneId.systemDefault()
) : TimeProvider {

    override fun startOfDay(): Long {
        val now = LocalDate.now(zone)
        return now.atStartOfDay(zone).toInstant().toEpochMilli()
    }

    override fun endOfDay(): Long {
        val now = LocalDate.now(zone)
        return now.plusDays(1).atStartOfDay(zone).toInstant().toEpochMilli() -
            Constants.Time.END_OF_DAY_OFFSET_MILLIS
    }

    override fun startOfThisWeek(): Long {
        val now = LocalDate.now(zone)
        val monday = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        return monday.atStartOfDay(zone).toInstant().toEpochMilli()
    }

    override fun startOfLastWeek(): Long {
        val now = LocalDate.now(zone)
        val mondayThisWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        val mondayLastWeek = mondayThisWeek.minusWeeks(1)
        return mondayLastWeek.atStartOfDay(zone).toInstant().toEpochMilli()
    }

    override fun endOfLastWeek(): Long {
        val now = LocalDate.now(zone)
        val mondayThisWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        return mondayThisWeek.atStartOfDay(zone).toInstant().toEpochMilli() -
            Constants.Time.END_OF_DAY_OFFSET_MILLIS
    }
}
