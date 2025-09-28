package com.kuro.notiflow.domain.models.notifications

data class NotificationStats(
    val totalCount: Int = 0,
    val unreadCount: Int = 0,
    val todayCount: Int = 0,
    val lastWeekCount: Int = 0,
    val thisWeekCount: Int = 0
) {
    fun getNotificationGrowthThisWeekVsLastWeek(): String {
        return when {
            // Special case: no data last week, but has data this week
            lastWeekCount == 0 && thisWeekCount > 0 -> "100.0%"

            // No data both weeks → can't calculate growth
            lastWeekCount == 0 -> "No data"

            else -> "%.2f%%".format(((thisWeekCount - lastWeekCount) / lastWeekCount.toDouble()) * 100)
        }
    }
}