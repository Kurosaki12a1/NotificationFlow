package com.kuro.notiflow.data.importer

import com.kuro.notiflow.domain.models.notifications.NotificationModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.ByteArrayInputStream

class NotificationCsvImporterTest {

    private val importer = NotificationCsvImporter()

    @Test
    fun `imports csv and ignores postTimeFormatted`() {
        val csv = """
            id,packageName,title,text,subText,bigText,summaryText,infoText,textLines,postTime,postTimeFormatted,priority,category,smallIconResId,iconBase64,groupKey,channelId,isRead,isBookmarked
            1,com.test,Title,Text,,,,,line1\\nline2,1700000000000,01/01/25 10:00,3,message,,base64,group,channel,true,false
        """.trimIndent()

        val result = importer.import(ByteArrayInputStream(csv.toByteArray()))

        assertEquals(1, result.size)
        val model = result.first()
        assertEquals("com.test", model.packageName)
        assertEquals("Title", model.title)
        assertEquals("Text", model.text)
        assertEquals(listOf("line1", "line2"), model.textLines)
        assertEquals(1_700_000_000_000, model.postTime)
        assertEquals(3, model.priority)
        assertEquals("message", model.category)
        assertTrue(model.isRead)
        assertEquals(false, model.isBookmarked)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `throws when required columns missing`() {
        val csv = """
            id,packageName,title,text
            1,com.test,Title,Text
        """.trimIndent()

        importer.import(ByteArrayInputStream(csv.toByteArray()))
    }

    @Test(expected = IllegalArgumentException::class)
    fun `throws when required values missing`() {
        val csv = """
            id,packageName,postTime,priority,category
            1,,1700000000000,3,message
        """.trimIndent()

        importer.import(ByteArrayInputStream(csv.toByteArray()))
    }
}
