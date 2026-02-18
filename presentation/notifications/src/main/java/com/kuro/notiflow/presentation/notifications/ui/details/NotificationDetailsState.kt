package com.kuro.notiflow.presentation.notifications.ui.details

import com.kuro.notiflow.domain.models.notifications.NotificationModel

data class NotificationDetailsState(
    val notification : NotificationModel? = null,
)