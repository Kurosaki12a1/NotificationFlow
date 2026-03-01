package com.kuro.notiflow.presentation.bookmark.ui.rules

import androidx.lifecycle.viewModelScope
import com.kuro.notiflow.domain.utils.AppLog
import com.kuro.notiflow.domain.models.app.AppSelectionItem
import com.kuro.notiflow.domain.models.bookmark.BookmarkRule
import com.kuro.notiflow.domain.models.bookmark.BookmarkRuleMatchField
import com.kuro.notiflow.domain.models.bookmark.BookmarkRuleMatchType
import com.kuro.notiflow.domain.use_case.DeleteBookmarkRuleUseCase
import com.kuro.notiflow.domain.use_case.FetchBookmarkRuleAppsUseCase
import com.kuro.notiflow.domain.use_case.FetchBookmarkRulesUseCase
import com.kuro.notiflow.domain.use_case.UpsertBookmarkRuleUseCase
import com.kuro.notiflow.presentation.common.base.BaseViewModel
import com.kuro.notiflow.presentation.common.utils.SnackBarType
import com.kuro.notiflow.presentation.bookmark.R
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
class BookmarkRulesViewModel @Inject constructor(
    private val fetchBookmarkRulesUseCase: FetchBookmarkRulesUseCase,
    private val fetchBookmarkRuleAppsUseCase: FetchBookmarkRuleAppsUseCase,
    private val upsertBookmarkRuleUseCase: UpsertBookmarkRuleUseCase,
    private val deleteBookmarkRuleUseCase: DeleteBookmarkRuleUseCase
) : BaseViewModel() {

    private val _state = MutableStateFlow(BookmarkRulesState())
    val state: StateFlow<BookmarkRulesState> = _state.asStateFlow()

    private val _events = MutableSharedFlow<BookmarkRulesEvent>()
    val events = _events.asSharedFlow()

    init {
        observeRules()
        loadAvailableApps()
    }

    private fun observeRules() {
        viewModelScope.launch {
            fetchBookmarkRulesUseCase().collect { rules ->
                _state.update { current -> current.copy(rules = rules) }
            }
        }
    }

    private fun loadAvailableApps() {
        viewModelScope.launch(Dispatchers.IO) {
            val apps = fetchBookmarkRuleAppsUseCase()
            _state.update { current -> current.copy(availableApps = apps) }
        }
    }

    fun onAppSelected(app: AppSelectionItem?) {
        _state.update { current -> current.copy(selectedApp = app) }
    }

    fun onKeywordChanged(keyword: String) {
        _state.update { current -> current.copy(keyword = keyword) }
    }

    fun onMatchFieldChanged(matchField: BookmarkRuleMatchField) {
        _state.update { current -> current.copy(matchField = matchField) }
    }

    fun onMatchTypeChanged(matchType: BookmarkRuleMatchType) {
        _state.update { current -> current.copy(matchType = matchType) }
    }

    fun onEditRule(rule: BookmarkRule) {
        val selectedApp = _state.value.availableApps
            .firstOrNull { it.packageName == rule.packageName }
            ?: rule.packageName?.let { packageName ->
                // Preserve the existing package selection even if it is not part of the
                // current picker list, so editing the rule does not silently widen it
                // to "All apps".
                AppSelectionItem(
                    packageName = packageName,
                    appName = packageName
                )
            }
        _state.update { current ->
            current.copy(
                editingRuleId = rule.id,
                selectedApp = selectedApp,
                keyword = rule.keyword,
                matchField = rule.matchField,
                matchType = rule.matchType
            )
        }
    }

    fun onCancelEdit() {
        resetEditor()
    }

    fun onSaveRule() {
        val currentState = _state.value
        // Keep package-only rules valid, but block empty all-app rules.
        if (currentState.selectedApp == null && currentState.keyword.isBlank()) return
        if (hasConflictingRule(currentState)) {
            viewModelScope.launch {
                _events.emit(
                    BookmarkRulesEvent.ShowSnackBar(
                        R.string.bookmark_rules_duplicate_error,
                        SnackBarType.ERROR
                    )
                )
            }
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { current -> current.copy(isSaving = true) }
            try {
                val isEditing = currentState.editingRuleId != null
                upsertBookmarkRuleUseCase(
                    BookmarkRule(
                        id = currentState.editingRuleId ?: 0L,
                        packageName = currentState.selectedApp?.packageName,
                        keyword = currentState.keyword,
                        matchField = currentState.matchField,
                        matchType = currentState.matchType
                    )
                )
                _events.emit(
                    BookmarkRulesEvent.ShowSnackBar(
                        messageResId = if (isEditing) {
                            R.string.bookmark_rules_update_success
                        } else {
                            R.string.bookmark_rules_save_success
                        },
                        type = SnackBarType.SUCCESS
                    )
                )
                resetEditor()
            } catch (ex: Exception) {
                ex.throwIfCancellation()
                AppLog.e(TAG, "Save bookmark rule failed", ex)
                _state.update { current -> current.copy(isSaving = false) }
                _events.emit(
                    BookmarkRulesEvent.ShowSnackBar(
                        if (ex is IllegalArgumentException) {
                            R.string.bookmark_rules_duplicate_error
                        } else {
                            R.string.bookmark_rules_save_failed
                        },
                        SnackBarType.ERROR
                    )
                )
            }
        }
    }

    fun onDeleteRule(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                deleteBookmarkRuleUseCase(id)
                if (_state.value.editingRuleId == id) {
                    resetEditor()
                }
                _events.emit(
                    BookmarkRulesEvent.ShowSnackBar(
                        R.string.bookmark_rules_delete_success,
                        SnackBarType.SUCCESS
                    )
                )
            } catch (ex: Exception) {
                ex.throwIfCancellation()
                AppLog.e(TAG, "Delete bookmark rule failed", ex)
                _events.emit(
                    BookmarkRulesEvent.ShowSnackBar(
                        R.string.bookmark_rules_delete_failed,
                        SnackBarType.ERROR
                    )
                )
            }
        }
    }

    fun onRuleEnabledChanged(rule: BookmarkRule, isEnabled: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                upsertBookmarkRuleUseCase(rule.copy(isEnabled = isEnabled))
            } catch (ex: Exception) {
                ex.throwIfCancellation()
                AppLog.e(TAG, "Update bookmark rule state failed", ex)
                _events.emit(
                    BookmarkRulesEvent.ShowSnackBar(
                        if (ex is IllegalArgumentException) {
                            R.string.bookmark_rules_duplicate_error
                        } else {
                            R.string.bookmark_rules_update_failed
                        },
                        SnackBarType.ERROR
                    )
                )
            }
        }
    }

    private fun hasConflictingRule(state: BookmarkRulesState): Boolean {
        val packageName = state.selectedApp?.packageName?.trim().orEmpty()
        val keyword = state.keyword.trim().lowercase()
        return state.rules.any { rule ->
            rule.id != state.editingRuleId &&
                rule.packageScopeOverlaps(packageName) &&
                rule.matchFieldScopeOverlaps(state.matchField) &&
                rule.keywordScopeOverlaps(keyword)
        }
    }

    private fun resetEditor() {
        _state.update { current ->
            current.copy(
                editingRuleId = null,
                keyword = "",
                selectedApp = null,
                matchField = BookmarkRuleMatchField.TITLE_OR_TEXT,
                matchType = BookmarkRuleMatchType.CONTAINS,
                isSaving = false
            )
        }
    }
}
