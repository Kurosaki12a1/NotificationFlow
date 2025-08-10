package com.kuro.notiflow.presentation.ui.notifications

import com.kuro.notiflow.domain.models.notifications.NotificationModel

data class NotificationsViewState (
    val listNotifications: List<NotificationModel> = listOf(),
    val failure: String? = null,
    val showFilter : Boolean = false
)