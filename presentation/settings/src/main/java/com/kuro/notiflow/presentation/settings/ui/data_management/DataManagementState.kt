package com.kuro.notiflow.presentation.settings.ui.data_management

data class DataManagementState(
    val isLoading: Boolean = false,
    val retentionDays: Int = 30
)
