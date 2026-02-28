package com.kuro.notiflow.presentation.bookmark.ui.main

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kuro.notiflow.domain.utils.AppLog
import com.kuro.notiflow.domain.models.notifications.NotificationModel
import com.kuro.notiflow.domain.use_case.FetchBookmarkedNotificationsUseCase
import com.kuro.notiflow.domain.use_case.SetNotificationBookmarkUseCase
import com.kuro.notiflow.presentation.common.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    fetchBookmarkedNotificationsUseCase: FetchBookmarkedNotificationsUseCase,
    private val setNotificationBookmarkUseCase: SetNotificationBookmarkUseCase
) : BaseViewModel() {

    val bookmarkedNotifications: Flow<PagingData<NotificationModel>> =
        fetchBookmarkedNotificationsUseCase().cachedIn(viewModelScope)

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