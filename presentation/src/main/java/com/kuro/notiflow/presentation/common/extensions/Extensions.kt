package com.kuro.notiflow.presentation.common.extensions

import android.content.Context
import android.os.Bundle
import com.kuro.notiflow.domain.models.notifications.NotificationModel
import com.kuro.notiflow.presentation.common.utils.packageNameMap
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters

/**
 * Returns the value associated with the given key as a String if the key exists
 * and the value is not null. Otherwise, returns null.
 *
 * @param key The key for the desired value.
 * @return The string value if present and non-null, or empty String otherwise.
 */
fun Bundle.string(key: String): String {
    return if (containsKey(key)) getString(key) ?: "" else ""
}

/**
 * Returns the value associated with the given key as a CharSequence converted to String
 * if the key exists and the value is not null. Otherwise, returns null.
 *
 * @param key The key for the desired value.
 * @return The CharSequence value as a string if present and non-null, or empty String otherwise.
 */
fun Bundle.charSequenceToString(key: String): String {
    return if (containsKey(key)) getCharSequence(key)?.toString() ?: "" else ""
}

/**
 * Returns the number of notifications in the list that were posted today
 * (from 00:00:00 to 23:59:59.999 in the system's default time zone).
 *
 * @receiver List of [NotificationModel] objects to filter.
 * @return The count of notifications whose [postTime] falls within today.
 */
fun List<NotificationModel>.countTodayNotifications(): Int {
    val zone = ZoneId.systemDefault()

    val startOfDay = LocalDate.now(zone)
        .atStartOfDay(zone)
        .toInstant()
        .toEpochMilli()

    val endOfDay = LocalDate.now(zone)
        .plusDays(1)
        .atStartOfDay(zone)
        .toInstant()
        .toEpochMilli() - 1

    return this.count { it.postTime in startOfDay..endOfDay }
}

/**
 * Returns the percentage growth in the number of notifications between this week and last week.
 *
 * A week is defined as starting on Monday and ending on Sunday, based on the system's default time zone.
 * - If last week has no notifications and this week has > 0 → returns "100.0%".
 * - If both weeks have 0 → returns "No data".
 * - Otherwise, returns the growth rate as a formatted percentage string (e.g., "25.00%").
 *
 * @receiver The list of [NotificationModel] to analyze.
 * @return A [String] representing the growth percentage or "No data" if last week has no records.
 */
fun List<NotificationModel>.notificationGrowthThisWeekVsLastWeek(): String {
    val zone = ZoneId.systemDefault()
    val now = LocalDate.now(zone)

    // Get the Monday of this week (if today is Monday, use today)
    val mondayThisWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    // Get the Monday of last week by subtracting 1 week
    val mondayLastWeek = mondayThisWeek.minusWeeks(1)
    // Get the Sunday of last week (day before this Monday)
    val sundayLastWeek = mondayThisWeek.minusDays(1)

    // Start of this week (Monday 00:00 in millis)
    val startThisWeek = mondayThisWeek.atStartOfDay(zone).toInstant().toEpochMilli()

    // Start of last week (Monday 00:00 in millis)
    val startLastWeek = mondayLastWeek.atStartOfDay(zone).toInstant().toEpochMilli()

    // End of last week (Sunday 23:59:59.999 in millis)
    val endLastWeek = sundayLastWeek.plusDays(1).atStartOfDay(zone).toInstant().toEpochMilli() - 1

    // Count notifications posted this week (from Monday)
    val thisWeekCount = count { it.postTime >= startThisWeek }

    // Count notifications posted last week (Monday to Sunday)
    val lastWeekCount = count { it.postTime in startLastWeek..endLastWeek }

    return when {
        // Special case: no data last week, but has data this week
        lastWeekCount == 0 && thisWeekCount > 0 -> "100.0%"

        // No data both weeks → can't calculate growth
        lastWeekCount == 0 -> "No data"

        else -> "%.2f%%".format(((thisWeekCount - lastWeekCount) / lastWeekCount.toDouble()) * 100)
    }
}

fun String.getAppName(context : Context) : String? {
    return try {
        val packageManager = context.packageManager
        val info = packageManager.getApplicationInfo(this, 0)
        packageManager.getApplicationLabel(info).toString()
    } catch (e: Exception) {
        packageNameMap[this] ?: this.split(".")
            .lastOrNull()
            ?.replaceFirstChar { it.uppercaseChar() }
        ?: "Unknown"
    }
}