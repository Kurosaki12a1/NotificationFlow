package com.kuro.notiflow.presentation.ui.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuro.notiflow.domain.use_case.FetchNotificationsUseCase
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
class NotificationsViewModel @Inject constructor(
    private val fetchNotificationsUseCase: FetchNotificationsUseCase
) : ViewModel() {
    private val _state: MutableStateFlow<NotificationsViewState> =
        MutableStateFlow(NotificationsViewState())
    val state: StateFlow<NotificationsViewState>
        get() = _state.asStateFlow()

    private val _count: MutableStateFlow<Int> = MutableStateFlow(0)
    val count: StateFlow<Int>
        get() = _count.asStateFlow()

    init {
        fetchNotifications()
    }

    private fun fetchNotifications() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchNotificationsUseCase().collectLatest { listNotifications ->
                _state.update { it.copy(listNotifications = listNotifications) }
                _count.value = listNotifications.size
            }
        }
    }

    fun toggleFilterPopUp() {
        _state.update { it.copy(showFilter = !_state.value.showFilter) }
    }
}