package com.kuro.notiflow.data.data_source.bookmark

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kuro.notiflow.data.data_source.entity.BookmarkRuleEntity
import com.kuro.notiflow.domain.Constants
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkRuleDao {
    @Query(
        "SELECT * FROM ${Constants.Database.BOOKMARK_RULE_TABLE} " +
            "ORDER BY ${Constants.Database.COLUMN_RULE_IS_ENABLED} DESC, ${Constants.Database.COLUMN_ID} DESC"
    )
    fun fetchAll(): Flow<List<BookmarkRuleEntity>>

    @Query(
        "SELECT * FROM ${Constants.Database.BOOKMARK_RULE_TABLE} " +
            "WHERE ${Constants.Database.COLUMN_RULE_IS_ENABLED} = 1 " +
            "ORDER BY ${Constants.Database.COLUMN_ID} DESC"
    )
    suspend fun getEnabledRules(): List<BookmarkRuleEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(rule: BookmarkRuleEntity): Long

    @Query(
        "DELETE FROM ${Constants.Database.BOOKMARK_RULE_TABLE} " +
            "WHERE ${Constants.Database.COLUMN_ID} = :id"
    )
    suspend fun deleteById(id: Long)
}
