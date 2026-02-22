package com.kuro.notiflow.presentation.settings.ui.data_management

import com.kuro.notiflow.presentation.common.utils.SnackBarType

sealed interface DataManagementEvent {
    data class ShowSnackBar(
        val messageResId: Int,
        val formatArgs: List<Any> = emptyList(),
        val type: SnackBarType = SnackBarType.SUCCESS
    ) : DataManagementEvent

    data class RequestExport(val fileName: String) : DataManagementEvent
}
