package com.kuro.notiflow.domain.utils

import java.time.ZoneId

interface TimeProvider {
    val zone: ZoneId

    /** Millis at the start of today (00:00). */
    fun startOfDay(): Long

    /** Millis at the end of today (23:59:59.999). */
    fun endOfDay(): Long

    /** Millis at Monday 00:00 of this week. */
    fun startOfThisWeek(): Long

    /** Millis at Monday 00:00 of last week. */
    fun startOfLastWeek(): Long

    /** Millis at Sunday 23:59:59.999 of last week. */
    fun endOfLastWeek(): Long
}