package com.kuro.notiflow.data.importer

import com.kuro.notiflow.domain.models.notifications.NotificationModel

object NotificationImportMapper {
    private const val KEY_PACKAGE_NAME = "packagename"
    private const val KEY_POST_TIME = "posttime"
    private const val KEY_PRIORITY = "priority"
    private const val KEY_CATEGORY = "category"
    private const val KEY_TEXT_LINES = "textlines"
    private const val KEY_TITLE = "title"
    private const val KEY_TEXT = "text"
    private const val KEY_SUB_TEXT = "subtext"
    private const val KEY_BIG_TEXT = "bigtext"
    private const val KEY_SUMMARY_TEXT = "summarytext"
    private const val KEY_INFO_TEXT = "infotext"
    private const val KEY_SMALL_ICON_RES_ID = "smalliconresid"
    private const val KEY_ICON_BASE64 = "iconbase64"
    private const val KEY_GROUP_KEY = "groupkey"
    private const val KEY_CHANNEL_ID = "channelid"
    private const val KEY_IS_READ = "isread"
    private const val KEY_IS_BOOKMARKED = "isbookmarked"

    fun headerMap(header: List<String>): Map<String, Int> {
        return header
            .mapIndexed { index, name -> normalizeHeader(name) to index }
            .toMap()
    }

    fun validateHeader(headerMap: Map<String, Int>) {
        val required = listOf(
            KEY_PACKAGE_NAME,
            KEY_POST_TIME,
            KEY_PRIORITY,
            KEY_CATEGORY
        )
        val missing = required.filterNot { headerMap.containsKey(it) }
        if (missing.isNotEmpty()) {
            throw IllegalArgumentException("Missing required columns: ${missing.joinToString()}")
        }
    }

    fun toNotification(
        row: List<String>,
        headerMap: Map<String, Int>
    ): NotificationModel {
        fun value(key: String): String? = headerMap[key]?.let { index ->
            row.getOrNull(index)?.trim()
        }

        val packageName = value(KEY_PACKAGE_NAME).orEmpty()
        val postTime = value(KEY_POST_TIME)?.toLongOrNull()
        val priority = value(KEY_PRIORITY)?.toIntOrNull()
        val category = value(KEY_CATEGORY).orEmpty()

        if (packageName.isBlank() || postTime == null || priority == null || category.isBlank()) {
            throw IllegalArgumentException("Invalid row format")
        }

        val textLinesRaw = value(KEY_TEXT_LINES).orEmpty()
        val textLines = if (textLinesRaw.isBlank()) {
            null
        } else {
            textLinesRaw
                .replace("\\\\n", "\n")
                .replace("\\n", "\n")
                .replace("\r\n", "\n")
                .split("\n")
                .map { it.trimEnd('\\') }
        }

        return NotificationModel(
            packageName = packageName,
            title = value(KEY_TITLE).ifBlankToNull(),
            text = value(KEY_TEXT).ifBlankToNull(),
            subText = value(KEY_SUB_TEXT).ifBlankToNull(),
            bigText = value(KEY_BIG_TEXT).ifBlankToNull(),
            summaryText = value(KEY_SUMMARY_TEXT).ifBlankToNull(),
            infoText = value(KEY_INFO_TEXT).ifBlankToNull(),
            textLines = textLines,
            postTime = postTime,
            priority = priority,
            category = category,
            smallIconResId = value(KEY_SMALL_ICON_RES_ID)?.toIntOrNull(),
            iconBase64 = value(KEY_ICON_BASE64).ifBlankToNull(),
            groupKey = value(KEY_GROUP_KEY).ifBlankToNull(),
            channelId = value(KEY_CHANNEL_ID).ifBlankToNull(),
            isRead = value(KEY_IS_READ).toBooleanStrictSafe(),
            isBookmarked = value(KEY_IS_BOOKMARKED).toBooleanStrictSafe()
        )
    }

    private fun normalizeHeader(header: String): String {
        return header.trim().removePrefix("\uFEFF").lowercase()
    }

    private fun String?.ifBlankToNull(): String? {
        val value = this?.trim().orEmpty()
        return if (value.isBlank()) null else value
    }

    private fun String?.toBooleanStrictSafe(): Boolean {
        return this?.trim()?.equals("true", ignoreCase = true) == true
    }
}
