package com.kuro.notiflow.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuro.notiflow.domain.use_case.FetchNotificationsUseCase
import com.kuro.notiflow.presentation.common.extensions.update
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
class HomeViewModel @Inject constructor(
    private val fetchNotificationsUseCase: FetchNotificationsUseCase
) : ViewModel() {

    private val _state: MutableStateFlow<HomeViewState> = MutableStateFlow(HomeViewState())
    val state: StateFlow<HomeViewState>
        get() = _state.asStateFlow()

    init {
        fetchNotifications()
    }

    private fun fetchNotifications() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchNotificationsUseCase().collectLatest { listNotifications ->
                _state.update { it.copy(listNotifications = listNotifications) }
            }
        }
    }
}