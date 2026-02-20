package com.kuro.notiflow.presentation.settings.ui.data_management

sealed interface DataManagementEvent {
    data class ShowSnackbar(val messageResId: Int) : DataManagementEvent
}
