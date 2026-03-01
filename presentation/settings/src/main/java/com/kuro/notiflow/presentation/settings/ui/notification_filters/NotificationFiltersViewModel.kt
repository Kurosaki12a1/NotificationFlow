package com.kuro.notiflow.presentation.settings.ui.notification_filters

import androidx.lifecycle.viewModelScope
import com.kuro.notiflow.domain.use_case.FetchInstalledAppsUseCase
import com.kuro.notiflow.domain.use_case.LoadNotificationFilterSettingsUseCase
import com.kuro.notiflow.domain.use_case.UpdateNotificationFilterSettingsUseCase
import com.kuro.notiflow.domain.models.app.AppSelectionItem
import com.kuro.notiflow.domain.models.notifications.NotificationFilterMode
import com.kuro.notiflow.domain.models.notifications.NotificationFilterSettings
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

    fun setAppAllowed(app: AppSelectionItem, isAllowed: Boolean) {
        val updatedPackages = _state.value.selectedPackages.toMutableSet().apply {
            if (isAllowed) add(app.packageName) else remove(app.packageName)
        }.toSet()
        _state.update { state ->
            state.copy(
                selectedPackages = updatedPackages
            )
        }
        saveFilters(updatedPackages)
    }

    private fun syncState() {
        val selectedPackages = when (persistedSettings.mode) {
            NotificationFilterMode.ALLOW_ALL -> installedApps.mapTo(mutableSetOf()) { it.packageName }
            NotificationFilterMode.ALLOW_LIST -> installedApps
                .asSequence()
                .map { it.packageName }
                .filter { it in persistedSettings.packageNames }
                .toSet()
            // Treat legacy block-list data as an allow-list complement.
            NotificationFilterMode.BLOCK_LIST -> installedApps
                .asSequence()
                .map { it.packageName }
                .filter { it !in persistedSettings.packageNames }
                .toSet()
        }
        _state.update { state ->
            state.copy(
                apps = installedApps,
                selectedPackages = selectedPackages,
                isLoading = false
            )
        }
    }

    private fun saveFilters(selectedPackages: Set<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            updateNotificationFilterSettingsUseCase(
                NotificationFilterSettings(
                    mode = toMode(selectedPackages),
                    packageNames = if (selectedPackages.size == installedApps.size) {
                        emptySet()
                    } else {
                        selectedPackages
                    }
                )
            )
        }
    }

    private fun toMode(selectedPackages: Set<String>): NotificationFilterMode {
        return if (selectedPackages.size == installedApps.size) {
            NotificationFilterMode.ALLOW_ALL
        } else {
            NotificationFilterMode.ALLOW_LIST
        }
    }
}
