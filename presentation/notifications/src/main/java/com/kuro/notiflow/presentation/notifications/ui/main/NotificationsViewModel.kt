package com.kuro.notiflow.presentation.notifications.ui.main

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kuro.notiflow.domain.Constants
import com.kuro.notiflow.domain.models.notifications.NotificationListFilter
import com.kuro.notiflow.domain.models.notifications.NotificationModel
import com.kuro.notiflow.domain.models.notifications.NotificationReadFilter
import com.kuro.notiflow.domain.models.notifications.NotificationTimeFilter
import com.kuro.notiflow.domain.use_case.AddNotificationUseCase
import com.kuro.notiflow.domain.use_case.DeleteNotificationUseCase
import com.kuro.notiflow.domain.use_case.FetchNotificationsUseCase
import com.kuro.notiflow.domain.use_case.GetDistinctNotificationPackagesUseCase
import com.kuro.notiflow.domain.use_case.GetOverviewNotificationStatsUseCase
import com.kuro.notiflow.domain.use_case.SetNotificationBookmarkUseCase
import com.kuro.notiflow.domain.use_case.SetNotificationReadUseCase
import com.kuro.notiflow.presentation.common.base.BaseViewModel
import com.kuro.notiflow.domain.utils.AppLog
import com.kuro.notiflow.presentation.common.utils.SnackBarType
import com.kuro.notiflow.presentation.notifications.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val fetchNotificationsUseCase: FetchNotificationsUseCase,
    private val getDistinctNotificationPackagesUseCase: GetDistinctNotificationPackagesUseCase,
    getOverviewNotificationStatsUseCase: GetOverviewNotificationStatsUseCase,
    private val deleteNotificationUseCase: DeleteNotificationUseCase,
    private val addNotificationUseCase: AddNotificationUseCase,
    private val setNotificationBookmarkUseCase: SetNotificationBookmarkUseCase,
    private val setNotificationReadUseCase: SetNotificationReadUseCase
) : BaseViewModel() {
    private val searchQuery = MutableStateFlow("")
    private val listFilter = MutableStateFlow(NotificationListFilter())
    private val _state: MutableStateFlow<NotificationsViewState> =
        MutableStateFlow(NotificationsViewState())
    val state: StateFlow<NotificationsViewState>
        get() = _state.asStateFlow()
    private val _events = MutableSharedFlow<NotificationsEvent>()
    val events = _events.asSharedFlow()

    val listNotifications: Flow<PagingData<NotificationModel>> = combine(
        searchQuery
            .debounce(Constants.Notifications.SEARCH_DEBOUNCE_MILLIS)
            .map { query ->
                val normalizedQuery = query.trim()
                if (normalizedQuery.length < Constants.Notifications.SEARCH_MIN_QUERY_LENGTH) {
                    ""
                } else {
                    normalizedQuery
                }
            }
            .distinctUntilChanged(),
        listFilter
    ) { query, filter ->
        query to filter
    }
        .distinctUntilChanged()
        .flatMapLatest { (query, filter) ->
            fetchNotificationsUseCase(query, filter)
        }
        .cachedIn(viewModelScope)

    val overviewNotificationStats: StateFlow<Int> = getOverviewNotificationStatsUseCase()
        .map { it.totalCount }
        .distinctUntilChanged()
        .stateIn(viewModelScope, SharingStarted.Lazily, 0)

    init {
        loadPackageOptions()
    }


    fun toggleFilterPopUp() {
        val next = !_state.value.showFilter
        AppLog.d(TAG, "toggleFilterPopUp -> $next")
        _state.update { it.copy(showFilter = next) }
    }

    fun onSearchQueryChanged(query: String) {
        if (query == _state.value.searchQuery) return
        _state.update { it.copy(searchQuery = query) }
        searchQuery.value = query
    }

    fun onPackageFilterChanged(packageName: String?) {
        val normalizedPackage = packageName?.trim().takeUnless { it.isNullOrEmpty() }
        if (normalizedPackage == _state.value.selectedPackageName) return
        _state.update { it.copy(selectedPackageName = normalizedPackage) }
        listFilter.update { it.copy(packageName = normalizedPackage) }
    }

    fun onReadFilterChanged(readFilter: NotificationReadFilter) {
        if (readFilter == _state.value.readFilter) return
        _state.update { it.copy(readFilter = readFilter) }
        listFilter.update { it.copy(readFilter = readFilter) }
    }

    fun onTimeFilterChanged(timeFilter: NotificationTimeFilter) {
        if (timeFilter == _state.value.timeFilter) return
        _state.update {
            it.copy(
                timeFilter = timeFilter,
                customStartTime = if (timeFilter == NotificationTimeFilter.CUSTOM) {
                    it.customStartTime
                } else {
                    null
                },
                customEndTime = if (timeFilter == NotificationTimeFilter.CUSTOM) {
                    it.customEndTime
                } else {
                    null
                }
            )
        }
        listFilter.update {
            it.copy(
                timeFilter = timeFilter,
                customStartTime = if (timeFilter == NotificationTimeFilter.CUSTOM) {
                    it.customStartTime
                } else {
                    null
                },
                customEndTime = if (timeFilter == NotificationTimeFilter.CUSTOM) {
                    it.customEndTime
                } else {
                    null
                }
            )
        }
    }

    fun onCustomTimeRangeChanged(startTime: Long?, endTime: Long?) {
        _state.update {
            it.copy(
                customStartTime = startTime,
                customEndTime = endTime
            )
        }
        listFilter.update {
            it.copy(
                customStartTime = startTime,
                customEndTime = endTime
            )
        }
    }

    fun resetListFilters() {
        _state.update {
            it.copy(
                selectedPackageName = null,
                readFilter = NotificationReadFilter.ALL,
                timeFilter = NotificationTimeFilter.ALL,
                customStartTime = null,
                customEndTime = null
            )
        }
        listFilter.value = NotificationListFilter()
    }

    fun refreshPackageOptions() {
        loadPackageOptions()
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

    fun toggleNotificationSelection(id: Long) {
        _state.update { state ->
            val selectedIds = state.selectedNotificationIds.toMutableSet()
            if (id in selectedIds) {
                selectedIds.remove(id)
            } else {
                selectedIds.add(id)
            }
            state.copy(
                selectedNotificationIds = selectedIds,
                swipingNotificationIds = state.swipingNotificationIds - id
            )
        }
    }

    fun clearSelection() {
        _state.update {
            it.copy(
                selectedNotificationIds = emptySet(),
                swipingNotificationIds = emptySet()
            )
        }
    }

    fun onItemSwipeStateChanged(id: Long, isSwiping: Boolean) {
        _state.update { state ->
            val swipingIds = state.swipingNotificationIds.toMutableSet()
            if (isSwiping) {
                swipingIds.add(id)
            } else {
                swipingIds.remove(id)
            }
            state.copy(swipingNotificationIds = swipingIds)
        }
    }

    fun onNotificationRemoved(id: Long) {
        _state.update {
            it.copy(
                selectedNotificationIds = it.selectedNotificationIds - id,
                swipingNotificationIds = it.swipingNotificationIds - id
            )
        }
    }

    fun deleteSelectedNotifications() {
        val selectedIds = _state.value.selectedNotificationIds
        if (selectedIds.isEmpty()) return
        AppLog.i(TAG, "deleteSelectedNotifications count=${selectedIds.size}")
        viewModelScope.launch(Dispatchers.IO) {
            var successCount = 0
            selectedIds.forEach { id ->
                runCatching { deleteNotificationUseCase(id) }
                    .onSuccess { successCount += 1 }
                    .onFailure { ex ->
                        if (ex is CancellationException) throw ex
                        AppLog.e(TAG, "Delete selected notification failed id=$id", ex)
                    }
            }
            if (successCount > 0) {
                _events.emit(
                    NotificationsEvent.ShowSnackBar(
                        type = SnackBarType.SUCCESS,
                        messageResId = R.plurals.notifications_selected_deleted,
                        quantity = successCount
                    )
                )
            }
        }
        clearSelection()
    }

    fun bookmarkSelectedNotifications() {
        val selectedIds = _state.value.selectedNotificationIds
        if (selectedIds.isEmpty()) return
        AppLog.i(TAG, "bookmarkSelectedNotifications count=${selectedIds.size}")
        viewModelScope.launch(Dispatchers.IO) {
            var successCount = 0
            selectedIds.forEach { id ->
                runCatching { setNotificationBookmarkUseCase(id, true) }
                    .onSuccess { successCount += 1 }
                    .onFailure { ex ->
                        if (ex is CancellationException) throw ex
                        AppLog.e(TAG, "Bookmark selected notification failed id=$id", ex)
                    }
            }
            if (successCount > 0) {
                _events.emit(
                    NotificationsEvent.ShowSnackBar(
                        type = SnackBarType.SUCCESS,
                        messageResId = R.plurals.notifications_selected_bookmarked,
                        quantity = successCount
                    )
                )
            }
        }
        clearSelection()
    }

    fun markSelectedNotificationsAsRead() {
        val selectedIds = _state.value.selectedNotificationIds
        if (selectedIds.isEmpty()) return
        AppLog.i(TAG, "markSelectedNotificationsAsRead count=${selectedIds.size}")
        viewModelScope.launch(Dispatchers.IO) {
            var successCount = 0
            selectedIds.forEach { id ->
                runCatching { setNotificationReadUseCase(id, true) }
                    .onSuccess { successCount += 1 }
                    .onFailure { ex ->
                        if (ex is CancellationException) throw ex
                        AppLog.e(TAG, "Mark selected notification as read failed id=$id", ex)
                    }
            }
            if (successCount > 0) {
                _events.emit(
                    NotificationsEvent.ShowSnackBar(
                        type = SnackBarType.SUCCESS,
                        messageResId = R.plurals.notifications_selected_marked_read,
                        quantity = successCount
                    )
                )
            }
        }
        clearSelection()
    }

    private fun loadPackageOptions() {
        viewModelScope.launch(Dispatchers.IO) {
            val packages = runCatching { getDistinctNotificationPackagesUseCase() }
                .getOrDefault(emptyList())
            _state.update { it.copy(packageOptions = packages) }
        }
    }
}

private const val TAG = "NotificationsViewModel"

sealed interface NotificationsEvent {
    data class ShowSnackBar(
        val type: SnackBarType,
        val messageResId: Int,
        val quantity: Int
    ) : NotificationsEvent
}
