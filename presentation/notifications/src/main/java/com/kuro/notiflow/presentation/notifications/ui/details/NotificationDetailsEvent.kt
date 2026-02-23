package com.kuro.notiflow.presentation.notifications.ui.details

import com.kuro.notiflow.presentation.common.utils.SnackBarType

sealed interface NotificationDetailsEvent {
    data class ShowSnackBar(
        val messageResId: Int,
        val type: SnackBarType = SnackBarType.SUCCESS
    ) : NotificationDetailsEvent
}
