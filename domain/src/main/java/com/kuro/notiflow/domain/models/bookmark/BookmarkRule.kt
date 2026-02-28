package com.kuro.notiflow.domain.models.bookmark

data class BookmarkRule(
    val id: Long = 0L,
    val packageName: String? = null,
    val keyword: String,
    val matchField: BookmarkRuleMatchField,
    val matchType: BookmarkRuleMatchType,
    val isEnabled: Boolean = true
)
