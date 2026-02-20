package com.kuro.notiflow.presentation.common.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuro.notiflow.domain.use_case.AutoClearNotificationsUseCase
import com.kuro.notiflow.domain.use_case.LoadSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val loadSettingsUseCase: LoadSettingsUseCase,
    private val autoClearNotificationsUseCase: AutoClearNotificationsUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(MainViewState())
    val state: StateFlow<MainViewState> = _state.asStateFlow()

    init {
        initState()
        runAutoClear()
    }

    private fun initState() {
        viewModelScope.launch(Dispatchers.IO) {
            loadSettingsUseCase().collectLatest { settings ->
                _state.value = _state.value.copy(settingsModel = settings)
            }
        }
    }

    private fun runAutoClear() {
        viewModelScope.launch(Dispatchers.IO) {
            autoClearNotificationsUseCase()
        }
    }
}
