package com.kuro.notiflow.presentation.common.ui.main

import androidx.lifecycle.viewModelScope
import com.kuro.notiflow.domain.use_case.AutoClearNotificationsUseCase
import com.kuro.notiflow.domain.use_case.LoadSettingsUseCase
import com.kuro.notiflow.domain.use_case.OnboardingUseCase
import com.kuro.notiflow.presentation.common.base.BaseViewModel
import com.kuro.notiflow.domain.logger.AppLog
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
    private val autoClearNotificationsUseCase: AutoClearNotificationsUseCase,
    private val onboardingUseCase: OnboardingUseCase
) : BaseViewModel() {
    private val _state = MutableStateFlow(MainViewState())
    val state: StateFlow<MainViewState> = _state.asStateFlow()

    init {
        initState()
        runAutoClear()
        observeFirstLaunch()
    }

    private fun initState() {
        AppLog.d(TAG, "initState")
        viewModelScope.launch(Dispatchers.IO) {
            loadSettingsUseCase().collectLatest { settings ->
                _state.value = _state.value.copy(settingsModel = settings)
            }
        }
    }

    private fun runAutoClear() {
        AppLog.d(TAG, "runAutoClear")
        viewModelScope.launch(Dispatchers.IO) {
            autoClearNotificationsUseCase()
        }
    }

    private fun observeFirstLaunch() {
        AppLog.d(TAG, "observeFirstLaunch")
        viewModelScope.launch(Dispatchers.IO) {
            onboardingUseCase.isFirstLaunch.collectLatest { isFirst ->
                _state.value = _state.value.copy(isFirstLaunch = isFirst)
            }
        }
    }
}
