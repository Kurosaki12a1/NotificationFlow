package com.kuro.notiflow.presentation.notifications.ui.settings

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kuro.notiflow.domain.models.notifications.NotificationModel
import com.kuro.notiflow.domain.use_case.FetchNotificationsUseCase
import com.kuro.notiflow.domain.use_case.GetOverviewNotificationStatsUseCase
import com.kuro.notiflow.presentation.common.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    fetchNotificationsUseCase: FetchNotificationsUseCase,
    getOverviewNotificationStatsUseCase: GetOverviewNotificationStatsUseCase
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
        _state.update { it.copy(showFilter = !_state.value.showFilter) }
    }
}
