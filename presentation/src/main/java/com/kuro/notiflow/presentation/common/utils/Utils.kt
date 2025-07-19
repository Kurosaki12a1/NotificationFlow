package com.kuro.notiflow.presentation.common.utils

import android.annotation.SuppressLint
import com.kuro.notiflow.domain.models.notifications.NotificationModel
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
}