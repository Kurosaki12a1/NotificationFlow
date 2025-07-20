package com.kuro.notiflow.presentation.common.utils

import android.annotation.SuppressLint
import android.content.Context
import com.kuro.notiflow.domain.models.notifications.NotificationModel
import com.kuro.notiflow.presentation.R
import com.kuro.notiflow.presentation.ui.home.PackageStats

object Utils {
    @SuppressLint("DefaultLocale")
    fun getTopRecentNotifications(notifications: List<NotificationModel>): List<PackageStats> {
        val total = notifications.size
        return notifications
            .groupingBy { it.packageName }
            .eachCount()
            .entries
            .sortedByDescending { it.value }
            .take(5)
            .map { (pkg, count) ->
                val percent = (count.toDouble() / total) * 100
                PackageStats(
                    packageName = pkg,
                    count = count,
                    percentage = String.format("%.2f", percent).toDouble()
                )
            }
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
            seconds < 60 -> context.getString(R.string.time_just_now)
            minutes < 60 -> context.resources.getQuantityString(R.plurals.time_minutes_ago, minutes.toInt(), minutes)
            hours < 24 -> context.resources.getQuantityString(R.plurals.time_hours_ago, hours.toInt(), hours)
            days < 7 -> context.resources.getQuantityString(R.plurals.time_days_ago, days.toInt(), days)
            weeks < 5 -> context.resources.getQuantityString(R.plurals.time_weeks_ago, weeks.toInt(), weeks)
            months < 12 -> context.resources.getQuantityString(R.plurals.time_months_ago, months.toInt(), months)
            else -> context.resources.getQuantityString(R.plurals.time_years_ago, years.toInt(), years)
        }
    }
}