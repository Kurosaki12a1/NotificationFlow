package com.kuro.notiflow.presentation.settings.ui.settings

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuro.notiflow.domain.models.settings.SettingsModel
import com.kuro.notiflow.domain.use_case.LoadSettingsUseCase
import com.kuro.notiflow.domain.use_case.ResetSettingsUseCase
import com.kuro.notiflow.domain.use_case.UpdateSettingsUseCase
import com.kuro.notiflow.presentation.common.extensions.update
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val resetSettingsUseCase: ResetSettingsUseCase,
    private val updateSettingsUseCase: UpdateSettingsUseCase,
    private val loadSettingsUseCase: LoadSettingsUseCase
) : ViewModel() {
    private val _state = mutableStateOf(SettingsViewState())
    val state: State<SettingsViewState>
        get() = _state

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