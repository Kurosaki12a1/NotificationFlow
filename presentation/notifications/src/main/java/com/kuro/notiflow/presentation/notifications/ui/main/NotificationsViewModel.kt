package com.kuro.notiflow.presentation.notifications.ui.main

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kuro.notiflow.domain.models.notifications.NotificationModel
import com.kuro.notiflow.domain.use_case.AddNotificationUseCase
import com.kuro.notiflow.domain.use_case.DeleteNotificationUseCase
import com.kuro.notiflow.domain.use_case.FetchNotificationsUseCase
import com.kuro.notiflow.domain.use_case.GetOverviewNotificationStatsUseCase
import com.kuro.notiflow.domain.use_case.SetNotificationBookmarkUseCase
import com.kuro.notiflow.presentation.common.base.BaseViewModel
import com.kuro.notiflow.domain.utils.AppLog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    fetchNotificationsUseCase: FetchNotificationsUseCase,
    getOverviewNotificationStatsUseCase: GetOverviewNotificationStatsUseCase,
    private val deleteNotificationUseCase: DeleteNotificationUseCase,
    private val addNotificationUseCase: AddNotificationUseCase,
    private val setNotificationBookmarkUseCase: SetNotificationBookmarkUseCase
) : BaseViewModel() {
    private val _state: MutableStateFlow<NotificationsViewState> =
        MutableStateFlow(NotificationsViewState())
    val state: StateFlow<NotificationsViewState>
        get() = _state.asStateFlow()

    val listNotifications: Flow<PagingData<NotificationModel>> =
        fetchNotificationsUseCase().cachedIn(viewModelScope)

    val overviewNotificationStats: StateFlow<Int> = getOverviewNotificationStatsUseCase()
        .map { it.totalCount }
        .distinctUntilChanged()
        .stateIn(viewModelScope, SharingStarted.Lazily, 0)


    fun toggleFilterPopUp() {
        val next = !_state.value.showFilter
        AppLog.d(TAG, "toggleFilterPopUp -> $next")
        _state.update { it.copy(showFilter = next) }
    }

    fun deleteNotification(id: Long) {
        AppLog.i(TAG, "deleteNotification id=$id")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                deleteNotificationUseCase(id)
            } catch (ex: Exception) {
                ex.throwIfCancellation()
                AppLog.e(TAG, "Delete notification failed", ex)
            }
        }
    }

    fun restoreNotification(notification: NotificationModel) {
        AppLog.i(TAG, "restoreNotification id=${notification.id}")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                addNotificationUseCase(notification)
            } catch (ex: Exception) {
                ex.throwIfCancellation()
                AppLog.e(TAG, "Restore notification failed", ex)
            }
        }
    }

    fun setNotificationBookmark(id: Long, isBookmarked: Boolean) {
        AppLog.i(TAG, "setNotificationBookmark id=$id value=$isBookmarked")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                setNotificationBookmarkUseCase(id, isBookmarked)
            } catch (ex: Exception) {
                ex.throwIfCancellation()
                AppLog.e(TAG, "Set bookmark failed", ex)
            }
        }
    }
}
