package com.kuro.notiflow.presentation.common.extensions

import android.content.Context
import android.os.Bundle
import com.kuro.notiflow.domain.models.notifications.NotificationModel
import com.kuro.notiflow.presentation.common.utils.packageNameMap
import java.time.LocalDate
import java.time.ZoneId

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

    return count { it.postTime in startOfDay..endOfDay }
}

fun String.getAppName(context: Context): String {
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
