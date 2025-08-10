package com.kuro.notiflow.presentation.ui.notifications

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val fetchNotificationsUseCase: FetchNotificationsUseCase
) : ViewModel() {
    private val _state: MutableState<NotificationsViewState> =
        mutableStateOf(NotificationsViewState())
    val state: State<NotificationsViewState>
        get() = _state

    private val _count : MutableStateFlow<Int> = MutableStateFlow(0)
    val count : StateFlow<Int>
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