package com.kuro.notiflow.data.importer

import com.kuro.notiflow.domain.models.notifications.NotificationModel

object NotificationImportMapper {
    fun headerMap(header: List<String>): Map<String, Int> {
        return header
            .mapIndexed { index, name -> normalizeHeader(name) to index }
            .toMap()
    }

    fun validateHeader(headerMap: Map<String, Int>) {
        val required = listOf("packagename", "posttime", "priority", "category")
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

        val packageName = value("packagename").orEmpty()
        val postTime = value("posttime")?.toLongOrNull()
        val priority = value("priority")?.toIntOrNull()
        val category = value("category").orEmpty()

        if (packageName.isBlank() || postTime == null || priority == null || category.isBlank()) {
            throw IllegalArgumentException("Invalid row format")
        }

        val textLinesRaw = value("textlines").orEmpty()
        val textLines = if (textLinesRaw.isBlank()) {
            null
        } else {
            textLinesRaw.split("\\n")
        }

        return NotificationModel(
            packageName = packageName,
            title = value("title").ifBlankToNull(),
            text = value("text").ifBlankToNull(),
            subText = value("subtext").ifBlankToNull(),
            bigText = value("bigtext").ifBlankToNull(),
            summaryText = value("summarytext").ifBlankToNull(),
            infoText = value("infotext").ifBlankToNull(),
            textLines = textLines,
            postTime = postTime,
            priority = priority,
            category = category,
            smallIconResId = value("smalliconresid")?.toIntOrNull(),
            iconBase64 = value("iconbase64").ifBlankToNull(),
            groupKey = value("groupkey").ifBlankToNull(),
            channelId = value("channelid").ifBlankToNull(),
            isRead = value("isread").toBooleanStrictSafe(),
            isBookmarked = value("isbookmarked").toBooleanStrictSafe()
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
