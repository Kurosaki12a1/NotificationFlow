package com.kuro.notiflow.presentation.home.ui

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kuro.notiflow.domain.models.notifications.NotificationModel
import com.kuro.notiflow.domain.models.notifications.NotificationStats
import com.kuro.notiflow.domain.models.notifications.PackageStats
import com.kuro.notiflow.domain.use_case.FetchNotificationsUseCase
import com.kuro.notiflow.domain.use_case.FetchTopNotificationsUseCase
import com.kuro.notiflow.domain.use_case.GetOverviewNotificationStatsUseCase
import com.kuro.notiflow.presentation.common.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    fetchNotificationsUseCase: FetchNotificationsUseCase,
    fetchTopNotificationsUseCase: FetchTopNotificationsUseCase,
    getOverviewNotificationStatsUseCase: GetOverviewNotificationStatsUseCase
) : BaseViewModel() {

    val overviewNotificationStats: StateFlow<NotificationStats> =
        getOverviewNotificationStatsUseCase()
            .stateIn(viewModelScope, SharingStarted.Lazily, NotificationStats())

    val topNotifications: StateFlow<List<PackageStats>> = fetchTopNotificationsUseCase()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val listNotifications: Flow<PagingData<NotificationModel>> =
        fetchNotificationsUseCase().cachedIn(viewModelScope)
}
