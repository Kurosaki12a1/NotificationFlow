package com.kuro.notiflow.presentation.bookmark.ui

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kuro.notiflow.domain.models.notifications.NotificationModel
import com.kuro.notiflow.domain.use_case.FetchBookmarkedNotificationsUseCase
import com.kuro.notiflow.presentation.common.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    fetchBookmarkedNotificationsUseCase: FetchBookmarkedNotificationsUseCase
) : BaseViewModel() {

    val bookmarkedNotifications: Flow<PagingData<NotificationModel>> =
        fetchBookmarkedNotificationsUseCase().cachedIn(viewModelScope)
}
