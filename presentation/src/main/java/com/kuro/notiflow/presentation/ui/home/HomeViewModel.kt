package com.kuro.notiflow.presentation.ui.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuro.notiflow.domain.use_case.FetchNotificationsUseCase
import com.kuro.notiflow.presentation.common.extensions.update
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fetchNotificationsUseCase: FetchNotificationsUseCase
) : ViewModel() {

    private val _state: MutableState<HomeViewState> = mutableStateOf(HomeViewState())
    val state: State<HomeViewState>
        get() = _state

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