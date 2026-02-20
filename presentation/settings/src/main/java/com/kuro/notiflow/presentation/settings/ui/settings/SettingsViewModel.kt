package com.kuro.notiflow.presentation.settings.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuro.notiflow.domain.models.settings.SettingsModel
import com.kuro.notiflow.domain.use_case.LoadSettingsUseCase
import com.kuro.notiflow.domain.use_case.ResetSettingsUseCase
import com.kuro.notiflow.domain.use_case.UpdateSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val resetSettingsUseCase: ResetSettingsUseCase,
    private val updateSettingsUseCase: UpdateSettingsUseCase,
    private val loadSettingsUseCase: LoadSettingsUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(SettingsViewState())
    val state: StateFlow<SettingsViewState> = _state.asStateFlow()

    init {
        initState()
    }

    private fun initState() {
        viewModelScope.launch(Dispatchers.IO) {
            loadSettingsUseCase().collectLatest { settings ->
                _state.update { it.copy(settingsModel = settings) }
            }
        }
    }

    fun updateSettings(newState: SettingsModel) {
        viewModelScope.launch(Dispatchers.IO) {
            updateSettingsUseCase(newState)
        }
    }

    fun resetToDefault() {
        viewModelScope.launch(Dispatchers.IO) {
            resetSettingsUseCase()
        }
    }
}
