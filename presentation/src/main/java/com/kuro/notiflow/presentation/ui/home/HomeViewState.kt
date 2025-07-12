package com.kuro.notiflow.presentation.ui.home

import com.kuro.notiflow.domain.models.notifications.NotificationModel

data class HomeViewState(
    val listNotification: List<NotificationModel> = listOf(),
    val failure: String? = null
)