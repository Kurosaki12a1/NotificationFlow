package com.kuro.notiflow.presentation.notifications.ui.details

import androidx.lifecycle.viewModelScope
import com.kuro.notiflow.domain.use_case.GetNotificationUseCase
import com.kuro.notiflow.domain.use_case.DeleteNotificationUseCase
import com.kuro.notiflow.domain.use_case.OpenAppUseCase
import com.kuro.notiflow.domain.use_case.SetNotificationBookmarkUseCase
import com.kuro.notiflow.domain.use_case.SetNotificationReadUseCase
import com.kuro.notiflow.presentation.common.base.BaseViewModel
import com.kuro.notiflow.domain.utils.AppLog
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
    private val deleteNotificationUseCase: DeleteNotificationUseCase,
    private val setNotificationBookmarkUseCase: SetNotificationBookmarkUseCase,
    private val setNotificationReadUseCase: SetNotificationReadUseCase,
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
            val notification = getNotificationUseCase(id)
            _state.update { it.copy(notification = notification) }
            if (notification?.isRead == false) {
                setNotificationReadUseCase(notification.id, true)
                _state.update { it.copy(notification = notification.copy(isRead = true)) }
            }
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

    fun onDeleteClick(id: Long) {
        AppLog.i(TAG, "onDeleteClick id=$id")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                deleteNotificationUseCase(id)
                _events.emit(
                    NotificationDetailsEvent.ShowSnackBar(
                        R.string.delete_notification_success
                    )
                )
                _events.emit(NotificationDetailsEvent.NavigateBack)
            } catch (ex: Exception) {
                ex.throwIfCancellation()
                AppLog.e(TAG, "Delete notification failed", ex)
                _events.emit(
                    NotificationDetailsEvent.ShowSnackBar(
                        R.string.delete_notification_failed,
                        type = SnackBarType.ERROR
                    )
                )
            }
        }
    }

    fun onBookmarkClicked(shouldBookmark: Boolean) {
        val current = _state.value.notification ?: return
        if (current.isBookmarked == shouldBookmark) return
        _state.update { it.copy(notification = current.copy(isBookmarked = shouldBookmark)) }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                setNotificationBookmarkUseCase(current.id, shouldBookmark)
                _events.emit(
                    NotificationDetailsEvent.ShowSnackBar(
                        if (shouldBookmark) {
                            R.string.bookmark_added
                        } else {
                            R.string.bookmark_removed
                        },
                        type = SnackBarType.SUCCESS
                    )
                )
            } catch (ex: Exception) {
                ex.throwIfCancellation()
                AppLog.e(TAG, "Bookmark update failed", ex)
                _state.update { it.copy(notification = current) }
                _events.emit(
                    NotificationDetailsEvent.ShowSnackBar(
                        R.string.bookmark_failed,
                        type = SnackBarType.ERROR
                    )
                )
            }
        }
    }

    fun onShareClicked() {
        val current = _state.value.notification ?: return
        viewModelScope.launch {
            _events.emit(NotificationDetailsEvent.ShareNotification(current))
        }
    }
}
