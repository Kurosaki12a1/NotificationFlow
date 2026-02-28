package com.kuro.notiflow.data.mapper

import com.kuro.notiflow.data.data_source.entity.BookmarkRuleEntity
import com.kuro.notiflow.domain.models.bookmark.BookmarkRule
import com.kuro.notiflow.domain.models.bookmark.BookmarkRuleMatchField
import com.kuro.notiflow.domain.models.bookmark.BookmarkRuleMatchType

fun BookmarkRuleEntity.toDomain(): BookmarkRule {
    return BookmarkRule(
        id = id,
        packageName = packageName,
        keyword = keyword,
        matchField = matchField.toBookmarkRuleMatchField(),
        matchType = matchType.toBookmarkRuleMatchType(),
        isEnabled = isEnabled
    )
}

fun BookmarkRule.toEntity(): BookmarkRuleEntity {
    return BookmarkRuleEntity(
        id = id,
        packageName = packageName,
        keyword = keyword,
        matchField = matchField.name,
        matchType = matchType.name,
        isEnabled = isEnabled
    )
}

private fun String.toBookmarkRuleMatchField(): BookmarkRuleMatchField {
    return runCatching { BookmarkRuleMatchField.valueOf(this) }
        .getOrDefault(BookmarkRuleMatchField.TITLE_OR_TEXT)
}

private fun String.toBookmarkRuleMatchType(): BookmarkRuleMatchType {
    return runCatching { BookmarkRuleMatchType.valueOf(this) }
        .getOrDefault(BookmarkRuleMatchType.CONTAINS)
}
