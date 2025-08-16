package com.kuro.notiflow.presentation.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuro.notiflow.domain.use_case.GetNotificationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class NotificationDetailsViewModel @Inject constructor(
    private val getNotificationUseCase: GetNotificationUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(NotificationDetailsState())
    val state: Flow<NotificationDetailsState>
        get() = _state.asStateFlow()

    fun getNotification(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(notification = getNotificationUseCase(id)) }
        }
    }

    fun onBookmarkClicked(shouldBookmark: Boolean) {

    }
}