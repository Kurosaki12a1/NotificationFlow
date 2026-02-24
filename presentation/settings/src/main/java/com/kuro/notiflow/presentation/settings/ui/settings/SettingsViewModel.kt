package com.kuro.notiflow.presentation.settings.ui.settings

import androidx.lifecycle.viewModelScope
import com.kuro.notiflow.domain.models.settings.SettingsModel
import com.kuro.notiflow.domain.use_case.LoadSettingsUseCase
import com.kuro.notiflow.domain.use_case.ResetSettingsUseCase
import com.kuro.notiflow.domain.use_case.UpdateSettingsUseCase
import com.kuro.notiflow.presentation.common.base.BaseViewModel
import com.kuro.notiflow.domain.utils.AppLog
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
) : BaseViewModel() {
    private val _state = MutableStateFlow(SettingsViewState())
    val state: StateFlow<SettingsViewState> = _state.asStateFlow()

    init {
        initState()
    }

    private fun initState() {
        AppLog.d(TAG, "initState")
        viewModelScope.launch(Dispatchers.IO) {
            loadSettingsUseCase().collectLatest { settings ->
                _state.update { it.copy(settingsModel = settings) }
            }
        }
    }

    fun updateSettings(newState: SettingsModel) {
        AppLog.i(
            TAG,
            "updateSettings lang=${newState.language} theme=${newState.themeType} " +
                "colors=${newState.colorsType} dynamic=${newState.isDynamicColorEnabled} " +
                "secure=${newState.secureMode} retention=${newState.dataRetentionDays}"
        )
        viewModelScope.launch(Dispatchers.IO) {
            updateSettingsUseCase(newState)
        }
    }

    fun resetToDefault() {
        AppLog.i(TAG, "resetToDefault")
        viewModelScope.launch(Dispatchers.IO) {
            resetSettingsUseCase()
        }
    }
}
