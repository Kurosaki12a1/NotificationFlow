package com.kuro.notiflow.data.data_source.bookmark

import com.kuro.notiflow.data.data_source.entity.BookmarkRuleEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface BookmarkRuleLocalDataSource {
    fun fetchRules(): Flow<List<BookmarkRuleEntity>>
    suspend fun getEnabledRules(): List<BookmarkRuleEntity>
    suspend fun upsertRule(rule: BookmarkRuleEntity): Long
    suspend fun deleteRule(id: Long)
}

class BookmarkRuleLocalDataSourceImpl @Inject constructor(
    private val dao: BookmarkRuleDao
) : BookmarkRuleLocalDataSource {
    override fun fetchRules(): Flow<List<BookmarkRuleEntity>> = dao.fetchAll()

    override suspend fun getEnabledRules(): List<BookmarkRuleEntity> = dao.getEnabledRules()

    override suspend fun upsertRule(rule: BookmarkRuleEntity): Long = dao.upsert(rule)

    override suspend fun deleteRule(id: Long) {
        dao.deleteById(id)
    }
}
