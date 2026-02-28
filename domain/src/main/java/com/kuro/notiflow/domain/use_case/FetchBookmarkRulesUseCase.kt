package com.kuro.notiflow.domain.use_case

import com.kuro.notiflow.domain.api.bookmark.BookmarkRuleRepository
import com.kuro.notiflow.domain.models.bookmark.BookmarkRule
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchBookmarkRulesUseCase @Inject constructor(
    private val repository: BookmarkRuleRepository
) {
    operator fun invoke(): Flow<List<BookmarkRule>> = repository.fetchRules()
}
