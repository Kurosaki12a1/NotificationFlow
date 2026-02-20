package com.kuro.notiflow.presentation.settings.ui.data_management

data class DataManagementState(
    val isLoading: Boolean = false,
    val retentionDays: Int = 90,
    val dialogRetentionMode: RetentionMode = RetentionMode.CUSTOM,
    val dialogRetentionDays: Int = 90,
    val isRetentionDialogVisible: Boolean = false
)

enum class RetentionMode {
    ALWAYS,
    CUSTOM
}
