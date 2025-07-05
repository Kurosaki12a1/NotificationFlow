package com.kuro.notiflow.presentation.ui.main

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuro.notiflow.domain.use_case.LoadSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val loadSettingsUseCase: LoadSettingsUseCase
) : ViewModel() {
    private val _state = mutableStateOf(MainViewState())
    val state: State<MainViewState>
        get() = _state

    init {
        initState()
    }

    private fun initState() {
        viewModelScope.launch(Dispatchers.IO) {
            loadSettingsUseCase().collectLatest { settings ->
                _state.value = _state.value.copy(
                    settingsModel = settings
                )
            }
        }
    }
}