package com.kuro.notiflow.presentation.ui.home

import com.kuro.notiflow.domain.models.notifications.NotificationModel

data class HomeViewState(
    val listNotifications: List<NotificationModel> = listOf(),
    val failure: String? = null
)

data class PackageStats(
    val packageName: String,
    val count: Int,
    val percentage: Double
)

