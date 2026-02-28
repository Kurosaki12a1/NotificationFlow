package com.kuro.notiflow.domain.use_case

import com.kuro.notiflow.domain.Constants
import com.kuro.notiflow.domain.api.bookmark.BookmarkRuleRepository
import com.kuro.notiflow.domain.models.bookmark.BookmarkRule
import javax.inject.Inject

class UpsertBookmarkRuleUseCase @Inject constructor(
    private val repository: BookmarkRuleRepository
) {
    suspend operator fun invoke(rule: BookmarkRule): Long {
        val keyword = rule.keyword.trim()
        require(keyword.length >= Constants.BookmarkRule.MIN_KEYWORD_LENGTH)
        val packageName = rule.packageName?.trim().takeUnless { it.isNullOrEmpty() }
        return repository.upsertRule(
            rule.copy(
                packageName = packageName,
                keyword = keyword
            )
        )
    }
}
