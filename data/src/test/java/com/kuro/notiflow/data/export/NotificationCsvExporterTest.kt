package com.kuro.notiflow.data.export

import com.kuro.notiflow.data.framework.exporter.NotificationCsvExporter
import com.kuro.notiflow.domain.Constants
import com.kuro.notiflow.domain.models.notifications.NotificationModel
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.ByteArrayOutputStream
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NotificationCsvExporterTest {

    private val exporter = NotificationCsvExporter()

    @Test
    fun `exports csv with bom header and formatted post time`() {
        val postTime = 1_700_000_000_000L
        val formatted = SimpleDateFormat(Constants.Export.TIME_FORMAT_PATTERN, Locale.US)
            .format(Date(postTime))
        val notification = NotificationModel(
            id = 7,
            packageName = "com.test",
            title = "Title",
            text = "Hello, \"World\"",
            subText = null,
            bigText = null,
            summaryText = null,
            infoText = null,
            textLines = listOf("line1", "line2"),
            postTime = postTime,
            priority = 2,
            category = "message",
            smallIconResId = null,
            iconBase64 = null,
            groupKey = null,
            channelId = null,
            isRead = false,
            isBookmarked = true
        )

        val output = ByteArrayOutputStream()
        exporter.export(listOf(notification), output)

        val csv = output.toString(Charset.forName("UTF-8"))
        assertTrue(csv.startsWith("\uFEFF"))
        assertTrue(csv.contains("postTimeFormatted"))
        assertTrue(csv.contains(formatted))
        assertTrue(csv.contains("\"Hello, \"\"World\"\"\""))
    }
}
