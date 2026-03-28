package com.kuro.notiflow.presentation.notifications.ui.filter

import com.kuro.notiflow.domain.models.notifications.NotificationReadFilter
import com.kuro.notiflow.domain.models.notifications.NotificationTimeFilter
import com.kuro.notiflow.presentation.common.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
internal class FilterViewModel @Inject constructor() : BaseViewModel() {
    private val _state = MutableStateFlow(FilterDraftState())
    val state: StateFlow<FilterDraftState> = _state.asStateFlow()

    private var isInitialized = false

    fun initializeFromApplied(
        packageName: String?,
        readFilter: NotificationReadFilter,
        timeFilter: NotificationTimeFilter,
        startTime: Long?,
        endTime: Long?
    ) {
        if (isInitialized) return
        isInitialized = true
        _state.value = FilterDraftState(
            packageName = packageName,
            readFilter = readFilter,
            timeFilter = timeFilter,
            startTime = startTime,
            endTime = endTime
        )
    }

    fun showAppPicker() {
        _state.update { it.copy(showAppPicker = true) }
    }

    fun dismissAppPicker() {
        _state.update { it.copy(showAppPicker = false) }
    }

    fun selectPackage(packageName: String?) {
        _state.update {
            it.copy(
                packageName = packageName,
                showAppPicker = false
            )
        }
    }

    fun selectReadFilter(readFilter: NotificationReadFilter) {
        _state.update { it.copy(readFilter = readFilter) }
    }

    fun selectTimeFilter(timeFilter: NotificationTimeFilter) {
        _state.update {
            it.copy(
                timeFilter = timeFilter,
                startTime = if (timeFilter == NotificationTimeFilter.CUSTOM) {
                    it.startTime
                } else {
                    null
                },
                endTime = if (timeFilter == NotificationTimeFilter.CUSTOM) {
                    it.endTime
                } else {
                    null
                }
            )
        }
    }

    fun showDatePicker(datePicker: FilterDatePicker) {
        _state.update { it.copy(activeDatePicker = datePicker) }
    }

    fun dismissDatePicker() {
        _state.update { it.copy(activeDatePicker = null) }
    }

    fun selectStartDate(startTime: Long?) {
        _state.update {
            it.copy(
                startTime = startTime,
                activeDatePicker = null
            )
        }
    }

    fun selectEndDate(endTime: Long?) {
        _state.update {
            it.copy(
                endTime = endTime,
                activeDatePicker = null
            )
        }
    }

    fun resetToDefaults() {
        _state.value = FilterDraftState()
    }
}
