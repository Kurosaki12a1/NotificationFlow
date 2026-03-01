package com.kuro.notiflow.presentation.common.utils

import android.content.Context
import android.content.Intent
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.kuro.notiflow.domain.Constants
import com.kuro.notiflow.presentation.common.R as CommonR

object Utils {
    fun convertMillisToTime(millis: Long): String {
        val sdf = SimpleDateFormat(Constants.DateFormat.TIME_SHORT, Locale.getDefault())
        return sdf.format(Date(millis))
    }

    fun convertMillisToTimeDetails(millis: Long): String {
        val sdf = SimpleDateFormat(Constants.DateFormat.TIME_DETAIL, Locale.getDefault())
        return sdf.format(Date(millis))
    }

    fun formatRelativeTime(context: Context, timeMillis: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - timeMillis

        val seconds = diff / Constants.Time.MILLIS_PER_SECOND
        val minutes = seconds / Constants.Time.SECONDS_PER_MINUTE
        val hours = minutes / Constants.Time.MINUTES_PER_HOUR
        val days = hours / Constants.Time.HOURS_PER_DAY
        val weeks = days / Constants.Time.DAYS_PER_WEEK
        val months = days / Constants.Time.DAYS_PER_MONTH
        val years = days / Constants.Time.DAYS_PER_YEAR

        return when {
            seconds < Constants.Time.SECONDS_PER_MINUTE -> context.getString(CommonR.string.time_just_now)
            minutes < Constants.Time.MINUTES_PER_HOUR -> context.resources.getQuantityString(
                CommonR.plurals.time_minutes_ago,
                minutes.toInt(),
                minutes
            )

            hours < Constants.Time.HOURS_PER_DAY -> context.resources.getQuantityString(
                CommonR.plurals.time_hours_ago,
                hours.toInt(),
                hours
            )

            days < Constants.Time.DAYS_PER_WEEK -> context.resources.getQuantityString(
                CommonR.plurals.time_days_ago,
                days.toInt(),
                days
            )

            weeks < Constants.Time.DAYS_PER_MONTH / Constants.Time.DAYS_PER_WEEK -> context.resources.getQuantityString(
                CommonR.plurals.time_weeks_ago,
                weeks.toInt(),
                weeks
            )

            months < Constants.Time.DAYS_PER_YEAR / Constants.Time.DAYS_PER_MONTH -> context.resources.getQuantityString(
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

    fun shareText(
        context: Context,
        text: String,
        chooserTitle: String
    ) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
        }
        context.startActivity(
            Intent.createChooser(shareIntent, chooserTitle)
        )
    }
}
