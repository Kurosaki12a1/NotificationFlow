package com.kuro.notiflow.presentation.notifications.ui.details

import com.kuro.notiflow.domain.models.notifications.NotificationModel
import com.kuro.notiflow.presentation.common.utils.SnackBarType

sealed interface NotificationDetailsEvent {
    data class ShowSnackBar(
        val messageResId: Int,
        val type: SnackBarType = SnackBarType.SUCCESS
    ) : NotificationDetailsEvent

    data class ShareNotification(val notification: NotificationModel) : NotificationDetailsEvent

    data object NavigateBack : NotificationDetailsEvent
}
