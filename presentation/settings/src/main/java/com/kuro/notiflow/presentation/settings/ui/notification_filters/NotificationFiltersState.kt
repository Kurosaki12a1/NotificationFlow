package com.kuro.notiflow.presentation.settings.ui.notification_filters

import com.kuro.notiflow.domain.models.app.AppSelectionItem
import com.kuro.notiflow.domain.models.notifications.NotificationFilterMode

internal data class NotificationFiltersState(
    val apps: List<AppSelectionItem> = emptyList(),
    val mode: NotificationFilterMode = NotificationFilterMode.ALLOW_ALL,
    val selectedPackages: Set<String> = emptySet(),
    val isDirty: Boolean = false,
    val isLoading: Boolean = true
)
