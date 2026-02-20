package com.kuro.notiflow.presentation.settings.ui.data_management

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuro.notiflow.domain.models.settings.SettingsModel
import com.kuro.notiflow.domain.use_case.ClearAllNotificationsUseCase
import com.kuro.notiflow.domain.use_case.LoadSettingsUseCase
import com.kuro.notiflow.domain.use_case.UpdateSettingsUseCase
import com.kuro.notiflow.presentation.settings.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DataManagementViewModel @Inject constructor(
    private val clearAllNotificationsUseCase: ClearAllNotificationsUseCase,
    private val loadSettingsUseCase: LoadSettingsUseCase,
    private val updateSettingsUseCase: UpdateSettingsUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(DataManagementState())
    val state: StateFlow<DataManagementState> = _state.asStateFlow()

    private val _events = MutableSharedFlow<DataManagementEvent>()
    val events = _events.asSharedFlow()

    private var currentSettings: SettingsModel? = null

    init {
        viewModelScope.launch(Dispatchers.IO) {
            loadSettingsUseCase().collectLatest { settings ->
                currentSettings = settings
                val mode = modeFromDays(settings.dataRetentionDays)
                _state.update {
                    it.copy(
                        retentionDays = settings.dataRetentionDays,
                        dialogRetentionMode = mode,
                        dialogRetentionDays = daysForDialog(settings.dataRetentionDays)
                    )
                }
            }
        }
    }

    fun onImportData() {
        // TODO: Implement import Excel flow
    }

    fun onExportData() {
        // TODO: Implement export Excel flow
    }

    fun onRetentionClick() {
        val current = _state.value
        val mode = modeFromDays(current.retentionDays)
        _state.update {
            it.copy(
                dialogRetentionMode = mode,
                dialogRetentionDays = daysForDialog(current.retentionDays),
                isRetentionDialogVisible = true
            )
        }
    }

    fun onRetentionDialogModeChanged(mode: RetentionMode) {
        _state.update { it.copy(dialogRetentionMode = mode) }
    }

    fun onRetentionDialogDaysChanged(days: Int) {
        _state.update { it.copy(dialogRetentionDays = days.coerceIn(1, MAX_RETENTION_DAYS)) }
    }

    fun onRetentionDialogCancel() {
        val current = _state.value
        val mode = modeFromDays(current.retentionDays)
        _state.update {
            it.copy(
                dialogRetentionMode = mode,
                dialogRetentionDays = daysForDialog(current.retentionDays),
                isRetentionDialogVisible = false
            )
        }
    }

    fun onRetentionDialogConfirm() {
        val current = _state.value
        val days = when (current.dialogRetentionMode) {
            RetentionMode.ALWAYS -> 0
            RetentionMode.CUSTOM -> current.dialogRetentionDays.coerceIn(1, MAX_RETENTION_DAYS)
        }
        _state.update {
            it.copy(
                retentionDays = days,
                isRetentionDialogVisible = false
            )
        }
        val base = currentSettings ?: SettingsModel()
        val updated = base.copy(dataRetentionDays = days)
        currentSettings = updated
        viewModelScope.launch(Dispatchers.IO) {
            updateSettingsUseCase(updated)
        }
    }

    fun onClearData() {
        if (_state.value.isLoading) return
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isLoading = true) }
            try {
                clearAllNotificationsUseCase()
                _events.emit(DataManagementEvent.ShowSnackbar(R.string.data_management_clear_success))
            } catch (ex: Exception) {
                _events.emit(DataManagementEvent.ShowSnackbar(R.string.data_management_clear_failed))
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun modeFromDays(days: Int): RetentionMode {
        return if (days <= 0) RetentionMode.ALWAYS else RetentionMode.CUSTOM
    }

    private fun daysForDialog(days: Int): Int {
        return if (days <= 0) DEFAULT_RETENTION_DAYS else days.coerceIn(1, MAX_RETENTION_DAYS)
    }

    companion object {
        private const val DEFAULT_RETENTION_DAYS = 90
        private const val MAX_RETENTION_DAYS = 90
    }
}
