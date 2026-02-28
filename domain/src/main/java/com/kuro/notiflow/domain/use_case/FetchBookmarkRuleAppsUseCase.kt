package com.kuro.notiflow.domain.use_case

import com.kuro.notiflow.domain.api.bookmark.BookmarkRuleRepository
import com.kuro.notiflow.domain.models.app.AppSelectionItem
import javax.inject.Inject

class FetchBookmarkRuleAppsUseCase @Inject constructor(
    private val repository: BookmarkRuleRepository
) {
    suspend operator fun invoke(): List<AppSelectionItem> = repository.fetchAvailableApps()
}
