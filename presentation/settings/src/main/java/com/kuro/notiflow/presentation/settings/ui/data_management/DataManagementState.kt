package com.kuro.notiflow.presentation.settings.ui.data_management

import com.kuro.notiflow.domain.Constants

data class DataManagementState(
    val isLoading: Boolean = false,
    val retentionDays: Int = Constants.Settings.DEFAULT_RETENTION_DAYS,
    val dialogRetentionMode: RetentionMode = RetentionMode.CUSTOM,
    val dialogRetentionDays: Int = Constants.Settings.DEFAULT_RETENTION_DAYS,
    val isRetentionDialogVisible: Boolean = false
)

enum class RetentionMode {
    ALWAYS,
    CUSTOM
}
