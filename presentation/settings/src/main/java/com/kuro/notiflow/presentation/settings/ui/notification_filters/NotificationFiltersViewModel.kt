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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.asSharedFlow
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
    private var areInstalledAppsLoaded = false
    private var areSettingsLoaded = false
    private var persistedSelectionByMode: MutableMap<NotificationFilterMode, Set<String>> =
        mutableMapOf(
            NotificationFilterMode.BLOCK_LIST to emptySet(),
            NotificationFilterMode.ALLOW_LIST to emptySet()
        )
    private var draftSelectionByMode: MutableMap<NotificationFilterMode, Set<String>> =
        mutableMapOf(
            NotificationFilterMode.BLOCK_LIST to emptySet(),
            NotificationFilterMode.ALLOW_LIST to emptySet()
        )

    private val _state = MutableStateFlow(NotificationFiltersState())
    val state: StateFlow<NotificationFiltersState> = _state.asStateFlow()
    private val _events = MutableSharedFlow<NotificationFiltersEvent>()
    val events = _events.asSharedFlow()

    init {
        observeFilterSettings()
        loadInstalledApps()
    }

    private fun observeFilterSettings() {
        viewModelScope.launch {
            loadNotificationFilterSettingsUseCase().collect { settings ->
                persistedSettings = settings
                areSettingsLoaded = true
                syncPersistedSelections()
                syncState()
            }
        }
    }

    private fun loadInstalledApps() {
        viewModelScope.launch(Dispatchers.IO) {
            installedApps = runCatching { fetchInstalledAppsUseCase() }
                .getOrDefault(emptyList())
            areInstalledAppsLoaded = true
            syncState()
        }
    }

    fun onModeChanged(mode: NotificationFilterMode) {
        if (_state.value.mode == mode) return
        val nextSelectedPackages = selectionForMode(mode)
        _state.update {
            it.copy(
                mode = mode,
                selectedPackages = nextSelectedPackages,
                isDirty = isDirty(mode, nextSelectedPackages)
            )
        }
    }

    fun onAppSelectionChanged(app: AppSelectionItem, isSelected: Boolean) {
        val mode = _state.value.mode
        if (!isSelectableMode(mode)) return
        val updatedPackages = selectionForMode(mode).toMutableSet().apply {
            if (isSelected) add(app.packageName) else remove(app.packageName)
        }.toSet()
        draftSelectionByMode[mode] = updatedPackages
        _state.update {
            it.copy(
                selectedPackages = updatedPackages,
                isDirty = isDirty(mode, updatedPackages)
            )
        }
    }

    fun resetSelection() {
        draftSelectionByMode[NotificationFilterMode.BLOCK_LIST] = emptySet()
        draftSelectionByMode[NotificationFilterMode.ALLOW_LIST] = emptySet()
        val mode = NotificationFilterMode.ALLOW_ALL
        val selectedPackages = emptySet<String>()
        _state.update {
            it.copy(
                mode = mode,
                selectedPackages = selectedPackages,
                isDirty = isDirty(mode, selectedPackages)
            )
        }
    }

    fun onApplyClick() {
        val current = _state.value
        val normalizedSettings = normalizedSettings(current.mode, selectionForMode(current.mode))
        viewModelScope.launch(Dispatchers.IO) {
            updateNotificationFilterSettingsUseCase(normalizedSettings)
        }
    }

    fun onBackRequested() {
        viewModelScope.launch {
            _events.emit(NotificationFiltersEvent.RequestExit)
        }
    }

    fun discardDraftChanges() {
        draftSelectionByMode = persistedSelectionByMode.toMutableMap()
        val persistedMode = persistedSettings.mode
        val persistedPackages = selectionForMode(persistedMode)
        _state.update {
            it.copy(
                mode = persistedMode,
                selectedPackages = persistedPackages,
                isDirty = false
            )
        }
    }

    private fun syncState() {
        _state.update { current ->
            val nextMode = if (current.isDirty) current.mode else persistedSettings.mode
            val nextPackages = selectionForMode(nextMode)
            current.copy(
                apps = installedApps,
                mode = nextMode,
                selectedPackages = nextPackages,
                isDirty = isDirty(nextMode, nextPackages),
                isLoading = !areInstalledAppsLoaded || !areSettingsLoaded
            )
        }
    }

    private fun normalizedSettings(
        mode: NotificationFilterMode,
        selectedPackages: Set<String>
    ): NotificationFilterSettings {
        return NotificationFilterSettings(
            mode = mode,
            packageNames = normalizedPackages(mode, selectedPackages)
        )
    }

    private fun syncPersistedSelections() {
        persistedSelectionByMode = mutableMapOf(
            NotificationFilterMode.BLOCK_LIST to emptySet(),
            NotificationFilterMode.ALLOW_LIST to emptySet()
        )
        if (isSelectableMode(persistedSettings.mode)) {
            persistedSelectionByMode[persistedSettings.mode] =
                normalizedPackages(persistedSettings.mode, persistedSettings.packageNames)
        }
        if (!_state.value.isDirty) {
            draftSelectionByMode = persistedSelectionByMode.toMutableMap()
        }
    }

    private fun selectionForMode(mode: NotificationFilterMode): Set<String> {
        return if (isSelectableMode(mode)) {
            draftSelectionByMode[mode].orEmpty()
        } else {
            emptySet()
        }
    }

    private fun isSelectableMode(mode: NotificationFilterMode): Boolean {
        return mode == NotificationFilterMode.BLOCK_LIST || mode == NotificationFilterMode.ALLOW_LIST
    }

    private fun normalizedPackages(
        mode: NotificationFilterMode,
        selectedPackages: Set<String>
    ): Set<String> {
        return if (mode == NotificationFilterMode.ALLOW_ALL) {
            emptySet()
        } else {
            selectedPackages
        }
    }

    private fun isDirty(
        mode: NotificationFilterMode,
        selectedPackages: Set<String>
    ): Boolean {
        val isModeChanged = mode != persistedSettings.mode
        val isCurrentModeSelectionChanged = if (isSelectableMode(mode)) {
            selectedPackages != persistedSelectionByMode[mode].orEmpty()
        } else {
            false
        }
        val isAnySelectionChanged = draftSelectionByMode.any { (draftMode, draftSelection) ->
            draftSelection != persistedSelectionByMode[draftMode].orEmpty()
        }
        return isModeChanged || isCurrentModeSelectionChanged || isAnySelectionChanged
    }
}

internal sealed interface NotificationFiltersEvent {
    data object RequestExit : NotificationFiltersEvent
}
