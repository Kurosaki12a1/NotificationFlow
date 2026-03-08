package com.kuro.notiflow.domain.models.notifications

data class NotificationListFilter(
    val packageName: String? = null,
    val readFilter: NotificationReadFilter = NotificationReadFilter.ALL,
    val timeFilter: NotificationTimeFilter = NotificationTimeFilter.ALL,
    val customStartTime: Long? = null,
    val customEndTime: Long? = null
)
