package com.kuro.notiflow.presentation.common.utils

import android.content.Context
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.kuro.notiflow.presentation.common.R as CommonR

object Utils {
    fun convertMillisToTime(millis: Long): String {
        val sdf = SimpleDateFormat("HH:mm - dd/MM/yyyy", Locale.getDefault())
        return sdf.format(Date(millis))
    }

    fun convertMillisToTimeDetails(millis: Long): String {
        val sdf = SimpleDateFormat("EEEE, dd/MM/yyyy HH:mm", Locale.getDefault())
        return sdf.format(Date(millis))
    }

    fun formatRelativeTime(context: Context, timeMillis: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - timeMillis

        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24
        val weeks = days / 7
        val months = days / 30
        val years = days / 365

        return when {
            seconds < 60 -> context.getString(CommonR.string.time_just_now)
            minutes < 60 -> context.resources.getQuantityString(
                CommonR.plurals.time_minutes_ago,
                minutes.toInt(),
                minutes
            )

            hours < 24 -> context.resources.getQuantityString(
                CommonR.plurals.time_hours_ago,
                hours.toInt(),
                hours
            )

            days < 7 -> context.resources.getQuantityString(
                CommonR.plurals.time_days_ago,
                days.toInt(),
                days
            )

            weeks < 5 -> context.resources.getQuantityString(
                CommonR.plurals.time_weeks_ago,
                weeks.toInt(),
                weeks
            )

            months < 12 -> context.resources.getQuantityString(
                CommonR.plurals.time_months_ago,
                months.toInt(),
                months
            )

            else -> context.resources.getQuantityString(
                CommonR.plurals.time_years_ago,
                years.toInt(),
                years
            )
        }
    }
}
