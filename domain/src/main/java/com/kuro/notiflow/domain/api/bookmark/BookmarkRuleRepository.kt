package com.kuro.notiflow.domain.api.bookmark

import com.kuro.notiflow.domain.models.app.AppSelectionItem
import com.kuro.notiflow.domain.models.bookmark.BookmarkRule
import kotlinx.coroutines.flow.Flow

interface BookmarkRuleRepository {
    fun fetchRules(): Flow<List<BookmarkRule>>
    suspend fun upsertRule(rule: BookmarkRule): Long
    suspend fun deleteRule(id: Long)
    suspend fun fetchAvailableApps(): List<AppSelectionItem>
}
