package com.kuro.notiflow.presentation.bookmark.ui.rules

import androidx.lifecycle.viewModelScope
import com.kuro.notiflow.domain.models.app.AppSelectionItem
import com.kuro.notiflow.domain.models.bookmark.BookmarkRule
import com.kuro.notiflow.domain.models.bookmark.BookmarkRuleMatchField
import com.kuro.notiflow.domain.models.bookmark.BookmarkRuleMatchType
import com.kuro.notiflow.domain.use_case.DeleteBookmarkRuleUseCase
import com.kuro.notiflow.domain.use_case.FetchBookmarkRuleAppsUseCase
import com.kuro.notiflow.domain.use_case.FetchBookmarkRulesUseCase
import com.kuro.notiflow.domain.use_case.UpsertBookmarkRuleUseCase
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
class BookmarkRulesViewModel @Inject constructor(
    fetchBookmarkRulesUseCase: FetchBookmarkRulesUseCase,
    private val fetchBookmarkRuleAppsUseCase: FetchBookmarkRuleAppsUseCase,
    private val upsertBookmarkRuleUseCase: UpsertBookmarkRuleUseCase,
    private val deleteBookmarkRuleUseCase: DeleteBookmarkRuleUseCase
) : BaseViewModel() {

    private val _state = MutableStateFlow(BookmarkRulesState())
    val state: StateFlow<BookmarkRulesState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            fetchBookmarkRulesUseCase().collect { rules ->
                _state.update { current -> current.copy(rules = rules) }
            }
        }
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

    fun onSaveRule() {
        val currentState = _state.value
        if (currentState.keyword.isBlank()) return
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { current -> current.copy(isSaving = true) }
            try {
                upsertBookmarkRuleUseCase(
                    BookmarkRule(
                        packageName = currentState.selectedApp?.packageName,
                        keyword = currentState.keyword,
                        matchField = currentState.matchField,
                        matchType = currentState.matchType
                    )
                )
                _state.update { current ->
                    current.copy(
                        keyword = "",
                        selectedApp = null,
                        matchField = BookmarkRuleMatchField.TITLE_OR_TEXT,
                        matchType = BookmarkRuleMatchType.CONTAINS,
                        isSaving = false
                    )
                }
            } catch (ex: Exception) {
                ex.throwIfCancellation()
                _state.update { current -> current.copy(isSaving = false) }
            }
        }
    }

    fun onDeleteRule(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteBookmarkRuleUseCase(id)
        }
    }

    fun onRuleEnabledChanged(rule: BookmarkRule, isEnabled: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            upsertBookmarkRuleUseCase(rule.copy(isEnabled = isEnabled))
        }
    }
}
