package com.kuro.notiflow.presentation.bookmark.ui.rules

import com.kuro.notiflow.domain.models.app.AppSelectionItem
import com.kuro.notiflow.domain.models.bookmark.BookmarkRule
import com.kuro.notiflow.domain.models.bookmark.BookmarkRuleMatchField
import com.kuro.notiflow.domain.models.bookmark.BookmarkRuleMatchType

data class BookmarkRulesState(
    val rules: List<BookmarkRule> = emptyList(),
    val availableApps: List<AppSelectionItem> = emptyList(),
    val selectedApp: AppSelectionItem? = null,
    val keyword: String = "",
    val matchField: BookmarkRuleMatchField = BookmarkRuleMatchField.TITLE_OR_TEXT,
    val matchType: BookmarkRuleMatchType = BookmarkRuleMatchType.CONTAINS,
    val isSaving: Boolean = false
)
