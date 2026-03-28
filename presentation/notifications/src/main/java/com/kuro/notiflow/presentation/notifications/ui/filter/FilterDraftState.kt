package com.kuro.notiflow.presentation.notifications.ui.filter

import com.kuro.notiflow.domain.models.notifications.NotificationReadFilter
import com.kuro.notiflow.domain.models.notifications.NotificationTimeFilter

internal data class FilterDraftState(
    val showAppPicker: Boolean = false,
    val packageName: String? = null,
    val readFilter: NotificationReadFilter = NotificationReadFilter.ALL,
    val timeFilter: NotificationTimeFilter = NotificationTimeFilter.ALL,
    val startTime: Long? = null,
    val endTime: Long? = null,
    val activeDatePicker: FilterDatePicker? = null
)

internal enum class FilterDatePicker {
    START,
    END
}

internal fun Long?.isLessThanOrEqualTo(other: Long?): Boolean {
    return this != null && other != null && this <= other
}

internal fun FilterDraftState.canApply(): Boolean {
    return timeFilter != NotificationTimeFilter.CUSTOM || startTime.isLessThanOrEqualTo(endTime)
}
