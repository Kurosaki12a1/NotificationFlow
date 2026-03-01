package com.kuro.notiflow.domain.models.notifications

data class NotificationStats(
    val totalCount: Int = 0,
    val unreadCount: Int = 0,
    val todayCount: Int = 0,
    val lastWeekCount: Int = 0,
    val thisWeekCount: Int = 0
)
