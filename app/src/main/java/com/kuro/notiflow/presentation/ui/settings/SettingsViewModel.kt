package com.kuro.notiflow.presentation.ui.settings

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class SettingsViewModel : ViewModel() {
    private val _state = mutableStateOf(SettingsViewState())
    val state: State<SettingsViewState>
        get() = _state
}