package com.kuro.notiflow.presentation.settings.ui.data_management

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuro.notiflow.domain.use_case.ClearAllNotificationsUseCase
import com.kuro.notiflow.presentation.settings.R
import com.kuro.notiflow.presentation.common.extensions.update
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DataManagementViewModel @Inject constructor(
    private val clearAllNotificationsUseCase: ClearAllNotificationsUseCase
) : ViewModel() {
    private val _state = mutableStateOf(DataManagementState())
    val state: State<DataManagementState>
        get() = _state

    private val _events = MutableSharedFlow<DataManagementEvent>()
    val events = _events.asSharedFlow()

    fun onImportData() {
        // TODO: Implement import Excel flow
    }

    fun onExportData() {
        // TODO: Implement export Excel flow
    }

    fun onRetentionClick() {
        // TODO: Implement retention selection
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
}
