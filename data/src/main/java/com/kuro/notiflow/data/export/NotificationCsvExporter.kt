package com.kuro.notiflow.data.export

import com.kuro.notiflow.domain.Constants
import com.kuro.notiflow.domain.models.notifications.NotificationModel
import java.io.OutputStream
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class NotificationCsvExporter @Inject constructor() {
    fun export(notifications: List<NotificationModel>, outputStream: OutputStream) {
        outputStream.bufferedWriter(Charset.forName("UTF-8")).use { writer ->
            // Write UTF-8 BOM for better compatibility with Excel and diacritics.
            writer.write('\uFEFF'.code)
            writer.appendLine(CSV_HEADER)
            val timeFormatter = SimpleDateFormat(Constants.Export.TIME_FORMAT_PATTERN, Locale.US)
            notifications.forEach { notification ->
                val formattedPostTime = timeFormatter.format(Date(notification.postTime))
                val row = listOf(
                    notification.id.toString(),
                    notification.packageName,
                    notification.title.orEmpty(),
                    notification.text.orEmpty(),
                    notification.subText.orEmpty(),
                    notification.bigText.orEmpty(),
                    notification.summaryText.orEmpty(),
                    notification.infoText.orEmpty(),
                    notification.textLines?.joinToString("\\n").orEmpty(),
                    notification.postTime.toString(),
                    formattedPostTime,
                    notification.priority.toString(),
                    notification.category,
                    notification.smallIconResId?.toString().orEmpty(),
                    notification.iconBase64.orEmpty(),
                    notification.groupKey.orEmpty(),
                    notification.channelId.orEmpty(),
                    notification.isRead.toString(),
                    notification.isBookmarked.toString()
                ).joinToString(",") { escapeCsv(it) }
                writer.appendLine(row)
            }
            writer.flush()
        }
    }

    private fun escapeCsv(value: String): String {
        val needsQuotes = value.contains(',') || value.contains('\n') || value.contains('\r') || value.contains('"')
        if (!needsQuotes) return value
        val escaped = value.replace("\"", "\"\"")
        return "\"$escaped\""
    }

    private companion object {
        private const val CSV_HEADER =
            "id,packageName,title,text,subText,bigText,summaryText,infoText,textLines,postTime,postTimeFormatted,priority,category,smallIconResId,iconBase64,groupKey,channelId,isRead,isBookmarked"
    }
}
