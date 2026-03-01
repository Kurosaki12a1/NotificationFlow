package com.kuro.notiflow.presentation.settings.ui.notification_filters

import androidx.lifecycle.viewModelScope
import com.kuro.notiflow.domain.models.app.AppSelectionItem
import com.kuro.notiflow.domain.models.notifications.NotificationFilterMode
import com.kuro.notiflow.domain.models.notifications.NotificationFilterSettings
import com.kuro.notiflow.domain.use_case.FetchInstalledAppsUseCase
import com.kuro.notiflow.domain.use_case.LoadNotificationFilterSettingsUseCase
import com.kuro.notiflow.domain.use_case.UpdateNotificationFilterSettingsUseCase
import com.kuro.notiflow.presentation.common.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class NotificationFiltersViewModel @Inject constructor(
    private val fetchInstalledAppsUseCase: FetchInstalledAppsUseCase,
    private val loadNotificationFilterSettingsUseCase: LoadNotificationFilterSettingsUseCase,
    private val updateNotificationFilterSettingsUseCase: UpdateNotificationFilterSettingsUseCase
) : BaseViewModel() {
    private var installedApps: List<AppSelectionItem> = emptyList()
    private var persistedSettings = NotificationFilterSettings()

    private val _state = MutableStateFlow(NotificationFiltersState())
    val state: StateFlow<NotificationFiltersState> = _state.asStateFlow()

    init {
        observeFilterSettings()
        loadInstalledApps()
    }

    private fun observeFilterSettings() {
        viewModelScope.launch {
            loadNotificationFilterSettingsUseCase().collect { settings ->
                persistedSettings = settings
                syncState()
            }
        }
    }

    private fun loadInstalledApps() {
        viewModelScope.launch(Dispatchers.IO) {
            installedApps = fetchInstalledAppsUseCase()
            syncState()
        }
    }

    fun onViewTypeChanged(viewType: NotificationFiltersViewType) {
        if (_state.value.viewType == viewType) return
        _state.update { state -> state.copy(viewType = viewType) }
    }

    fun setAppBlocked(app: AppSelectionItem, isBlocked: Boolean) {
        val updatedPackages = _state.value.blockedPackages.toMutableSet().apply {
            if (isBlocked) add(app.packageName) else remove(app.packageName)
        }.toSet()
        _state.update { state ->
            state.copy(blockedPackages = updatedPackages)
        }
        saveFilters(updatedPackages)
    }

    private fun syncState() {
        val blockedPackages = when (persistedSettings.mode) {
            NotificationFilterMode.ALLOW_ALL -> emptySet()
            // Treat legacy allow-list data as a blocked complement of installed apps.
            NotificationFilterMode.ALLOW_LIST -> installedApps
                .asSequence()
                .map { it.packageName }
                .filter { it !in persistedSettings.packageNames }
                .toSet()
            NotificationFilterMode.BLOCK_LIST -> installedApps
                .asSequence()
                .map { it.packageName }
                .filter { it in persistedSettings.packageNames }
                .toSet()
        }
        _state.update { state ->
            state.copy(
                apps = installedApps,
                blockedPackages = blockedPackages,
                isLoading = false
            )
        }
    }

    private fun saveFilters(blockedPackages: Set<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            updateNotificationFilterSettingsUseCase(
                NotificationFilterSettings(
                    mode = if (blockedPackages.isEmpty()) {
                        NotificationFilterMode.ALLOW_ALL
                    } else {
                        NotificationFilterMode.BLOCK_LIST
                    },
                    packageNames = blockedPackages
                )
            )
        }
    }
}
