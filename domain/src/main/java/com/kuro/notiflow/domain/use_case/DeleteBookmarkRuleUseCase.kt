package com.kuro.notiflow.domain.use_case

import com.kuro.notiflow.domain.api.bookmark.BookmarkRuleRepository
import javax.inject.Inject

class DeleteBookmarkRuleUseCase @Inject constructor(
    private val repository: BookmarkRuleRepository
) {
    suspend operator fun invoke(id: Long) = repository.deleteRule(id)
}
