package com.kuro.notiflow.data.data_source.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kuro.notiflow.domain.Constants

@Entity(tableName = Constants.Database.BOOKMARK_RULE_TABLE)
data class BookmarkRuleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val packageName: String?,
    val keyword: String,
    val matchField: String,
    val matchType: String,
    val isEnabled: Boolean
)
