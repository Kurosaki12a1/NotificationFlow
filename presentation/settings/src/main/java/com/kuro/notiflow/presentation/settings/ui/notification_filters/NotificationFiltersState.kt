package com.kuro.notiflow.presentation.settings.ui.notification_filters

import com.kuro.notiflow.domain.models.app.AppSelectionItem

internal data class NotificationFiltersState(
    val apps: List<AppSelectionItem> = emptyList(),
    val selectedPackages: Set<String> = emptySet(),
    val viewType: NotificationFiltersViewType = NotificationFiltersViewType.ALL_APPS,
    val isLoading: Boolean = true
)
