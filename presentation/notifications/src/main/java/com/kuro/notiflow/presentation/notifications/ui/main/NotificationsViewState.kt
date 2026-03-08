package com.kuro.notiflow.presentation.notifications.ui.main

import com.kuro.notiflow.domain.models.notifications.NotificationReadFilter
import com.kuro.notiflow.domain.models.notifications.NotificationTimeFilter

data class NotificationsViewState(
    val showFilter: Boolean = false,
    val searchQuery: String = "",
    val selectedPackageName: String? = null,
    val readFilter: NotificationReadFilter = NotificationReadFilter.ALL,
    val timeFilter: NotificationTimeFilter = NotificationTimeFilter.ALL,
    val customStartTime: Long? = null,
    val customEndTime: Long? = null,
    val packageOptions: List<String> = emptyList(),
    val selectedNotificationIds: Set<Long> = emptySet(),
    val swipingNotificationIds: Set<Long> = emptySet()
) {
    val selectedCount: Int
        get() = selectedNotificationIds.size

    val isSelectionMode: Boolean
        get() = selectedCount > 0
}
