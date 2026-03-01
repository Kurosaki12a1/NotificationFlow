package com.kuro.notiflow.domain.models.bookmark

data class BookmarkRule(
    val id: Long = 0L,
    val packageName: String? = null,
    val keyword: String,
    val matchField: BookmarkRuleMatchField,
    val matchType: BookmarkRuleMatchType,
    val isEnabled: Boolean = true
) {

    fun packageScopeOverlaps(otherPackageName: String): Boolean {
        val normalizedPackageName = packageName.orEmpty().trim()
        return normalizedPackageName.isEmpty() ||
            otherPackageName.isEmpty() ||
            normalizedPackageName == otherPackageName
    }

    fun matchFieldScopeOverlaps(otherMatchField: BookmarkRuleMatchField): Boolean {
        return matchField == otherMatchField ||
            matchField == BookmarkRuleMatchField.TITLE_OR_TEXT ||
            otherMatchField == BookmarkRuleMatchField.TITLE_OR_TEXT
    }

    fun keywordScopeOverlaps(otherKeyword: String): Boolean {
        val normalizedKeyword = keyword.trim().lowercase()
        return normalizedKeyword.isEmpty() ||
            otherKeyword.isEmpty() ||
            normalizedKeyword == otherKeyword
    }
}
