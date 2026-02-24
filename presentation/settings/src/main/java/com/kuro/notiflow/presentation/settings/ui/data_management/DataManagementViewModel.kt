package com.kuro.notiflow.presentation.settings.ui.data_management

import androidx.lifecycle.viewModelScope
import com.kuro.notiflow.domain.Constants
import com.kuro.notiflow.domain.models.settings.SettingsModel
import com.kuro.notiflow.domain.use_case.ClearAllNotificationsUseCase
import com.kuro.notiflow.domain.use_case.ExportNotificationsUseCase
import com.kuro.notiflow.domain.use_case.ImportNotificationsUseCase
import com.kuro.notiflow.domain.use_case.LoadSettingsUseCase
import com.kuro.notiflow.domain.use_case.UpdateSettingsUseCase
import com.kuro.notiflow.presentation.common.base.BaseViewModel
import com.kuro.notiflow.domain.utils.AppLog
import com.kuro.notiflow.presentation.settings.R
import com.kuro.notiflow.presentation.common.utils.SnackBarType
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
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class DataManagementViewModel @Inject constructor(
    private val clearAllNotificationsUseCase: ClearAllNotificationsUseCase,
    private val loadSettingsUseCase: LoadSettingsUseCase,
    private val updateSettingsUseCase: UpdateSettingsUseCase,
    private val exportNotificationsUseCase: ExportNotificationsUseCase,
    private val importNotificationsUseCase: ImportNotificationsUseCase
) : BaseViewModel() {
    private val _state = MutableStateFlow(DataManagementState())
    val state: StateFlow<DataManagementState> = _state.asStateFlow()

    private val _events = MutableSharedFlow<DataManagementEvent>()
    val events = _events.asSharedFlow()

    private var currentSettings: SettingsModel? = null

    init {
        initData()
    }

    private fun initData() {
        viewModelScope.launch(Dispatchers.IO) {
            AppLog.d(TAG, "initData")
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

    fun onImportClick() {
        if (_state.value.isLoading) return
        AppLog.d(TAG, "onImportClick")
        viewModelScope.launch {
            _events.emit(DataManagementEvent.RequestImport)
        }
    }

    fun onImportData(targetUriString: String) {
        if (_state.value.isLoading) return
        AppLog.i(TAG, "onImportData uri=$targetUriString")
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isLoading = true) }
            try {
                val result = importNotificationsUseCase(targetUriString)
                if (result.isSuccess) {
                    val count = result.getOrNull() ?: 0
                    _events.emit(
                        DataManagementEvent.ShowSnackBar(
                            R.string.data_management_import_success,
                            listOf(count),
                            type = SnackBarType.SUCCESS
                        )
                    )
                } else {
                    _events.emit(
                        DataManagementEvent.ShowSnackBar(
                            R.string.data_management_import_failed,
                            type = SnackBarType.ERROR
                        )
                    )
                }
            } catch (ex: Exception) {
                ex.throwIfCancellation()
                AppLog.e(TAG, "Import failed", ex)
                _events.emit(
                    DataManagementEvent.ShowSnackBar(
                        R.string.data_management_import_failed,
                        type = SnackBarType.ERROR
                    )
                )
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onExportClick() {
        if (_state.value.isLoading) return
        AppLog.d(TAG, "onExportClick")
        val fileName = buildExportFileName()
        _state.update { it.copy(exportFileName = fileName) }
        viewModelScope.launch {
            _events.emit(DataManagementEvent.RequestExport(fileName))
        }
    }

    fun onExportData(targetUriString: String) {
        if (_state.value.isLoading) return
        AppLog.i(TAG, "onExportData uri=$targetUriString")
        val fileName = _state.value.exportFileName.ifBlank { buildExportFileName() }
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isLoading = true) }
            try {
                val result = exportNotificationsUseCase(targetUriString, fileName)
                if (result.isSuccess) {
                    val exportResult = result.getOrNull()
                    val exportedFileName = exportResult?.fileName ?: fileName
                    _events.emit(
                        DataManagementEvent.ShowSnackBar(
                            R.string.data_management_export_success,
                            listOf(exportedFileName),
                            type = SnackBarType.SUCCESS
                        )
                    )
                } else {
                    _events.emit(
                        DataManagementEvent.ShowSnackBar(
                            R.string.data_management_export_failed,
                            type = SnackBarType.ERROR
                        )
                    )
                }
            } catch (ex: Exception) {
                ex.throwIfCancellation()
                AppLog.e(TAG, "Export failed", ex)
                _events.emit(
                    DataManagementEvent.ShowSnackBar(
                        R.string.data_management_export_failed,
                        type = SnackBarType.ERROR
                    )
                )
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onRetentionClick() {
        AppLog.d(TAG, "onRetentionClick")
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
        AppLog.d(TAG, "onRetentionDialogModeChanged")
        _state.update { it.copy(dialogRetentionMode = mode) }
    }

    fun onRetentionDialogDaysChanged(days: Int) {
        AppLog.d(TAG, "onRetentionDialogDaysChanged")
        _state.update {
            it.copy(
                dialogRetentionDays = days.coerceIn(
                    Constants.Settings.MIN_RETENTION_DAYS,
                    Constants.Settings.MAX_RETENTION_DAYS
                )
            )
        }
    }

    fun onRetentionDialogCancel() {
        AppLog.d(TAG, "onRetentionDialogCancel")
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
            RetentionMode.CUSTOM -> current.dialogRetentionDays.coerceIn(
                Constants.Settings.MIN_RETENTION_DAYS,
                Constants.Settings.MAX_RETENTION_DAYS
            )
        }
        AppLog.i(TAG, "retentionConfirm days=$days")
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
        AppLog.i(TAG, "onClearData")
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isLoading = true) }
            try {
                clearAllNotificationsUseCase()
                _events.emit(
                    DataManagementEvent.ShowSnackBar(
                        R.string.data_management_clear_success,
                        type = SnackBarType.SUCCESS
                    )
                )
            } catch (ex: Exception) {
                ex.throwIfCancellation()
                AppLog.e(TAG, "Clear data failed", ex)
                _events.emit(
                    DataManagementEvent.ShowSnackBar(
                        R.string.data_management_clear_failed,
                        type = SnackBarType.ERROR
                    )
                )
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun modeFromDays(days: Int): RetentionMode {
        return if (days <= 0) RetentionMode.ALWAYS else RetentionMode.CUSTOM
    }

    private fun daysForDialog(days: Int): Int {
        return if (days <= 0) {
            Constants.Settings.DEFAULT_RETENTION_DAYS
        } else {
            days.coerceIn(
                Constants.Settings.MIN_RETENTION_DAYS,
                Constants.Settings.MAX_RETENTION_DAYS
            )
        }
    }

    private fun buildExportFileName(): String {
        val formatter = SimpleDateFormat(Constants.Export.TIMESTAMP_PATTERN, Locale.US)
        val stamp = formatter.format(System.currentTimeMillis())
        return "${Constants.Export.BASE_FILE_NAME}_$stamp.${Constants.Export.FILE_EXTENSION}"
    }

}
