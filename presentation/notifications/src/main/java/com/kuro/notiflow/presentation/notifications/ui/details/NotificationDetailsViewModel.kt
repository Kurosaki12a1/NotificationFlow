package com.kuro.notiflow.presentation.notifications.ui.details

import androidx.lifecycle.viewModelScope
import com.kuro.notiflow.domain.use_case.GetNotificationUseCase
import com.kuro.notiflow.domain.use_case.OpenAppUseCase
import com.kuro.notiflow.presentation.common.base.BaseViewModel
import com.kuro.notiflow.domain.logger.AppLog
import com.kuro.notiflow.domain.models.app.AppLaunchResult
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.kuro.notiflow.presentation.common.utils.SnackBarType
import com.kuro.notiflow.presentation.notifications.R

@HiltViewModel
class NotificationDetailsViewModel @Inject constructor(
    private val getNotificationUseCase: GetNotificationUseCase,
    private val openAppUseCase: OpenAppUseCase
) : BaseViewModel() {
    private val _state = MutableStateFlow(NotificationDetailsState())
    val state: Flow<NotificationDetailsState>
        get() = _state.asStateFlow()

    private val _events = MutableSharedFlow<NotificationDetailsEvent>()
    val events = _events.asSharedFlow()

    fun getNotification(id: Long) {
        AppLog.d(TAG, "getNotification")
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(notification = getNotificationUseCase(id)) }
        }
    }

    fun onSeeMoreClick(packageName: String) {
        AppLog.d(TAG, "onSeeMoreClick pkg=$packageName")
        viewModelScope.launch {
            val result = openAppUseCase(packageName)
            if (result == AppLaunchResult.FAILED) {
                _events.emit(
                    NotificationDetailsEvent.ShowSnackBar(
                        R.string.open_app_failed,
                        type = SnackBarType.ERROR
                    )
                )
            }
        }
    }

    fun onBookmarkClicked(shouldBookmark: Boolean) {

    }

    fun onShareClicked(id: Long) {

    }
}
