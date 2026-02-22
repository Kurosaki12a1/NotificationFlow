package com.kuro.notiflow.data.importer

import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class NotificationExcelImporterTest {

    private val importer = NotificationExcelImporter()

    @Test
    fun `imports xlsx and ignores postTimeFormatted`() {
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("Sheet1")
        val header = listOf(
            "id","packageName","title","text","subText","bigText","summaryText","infoText","textLines",
            "postTime","postTimeFormatted","priority","category","smallIconResId","iconBase64",
            "groupKey","channelId","isRead","isBookmarked"
        )
        val headerRow = sheet.createRow(0)
        header.forEachIndexed { index, value ->
            headerRow.createCell(index).setCellValue(value)
        }
        val row = sheet.createRow(1)
        val values = listOf(
            "1","com.test","Title","Text","","","","","line1\\nline2",
            "1700000000000","01/01/25 10:00","3","message","","base64",
            "group","channel","true","false"
        )
        values.forEachIndexed { index, value ->
            row.createCell(index).setCellValue(value)
        }

        val out = ByteArrayOutputStream()
        workbook.use { it.write(out) }

        val result = importer.import(ByteArrayInputStream(out.toByteArray()))

        assertEquals(1, result.size)
        val model = result.first()
        assertEquals("com.test", model.packageName)
        assertEquals("Title", model.title)
        assertEquals("Text", model.text)
        assertEquals(listOf("line1", "line2"), model.textLines)
        assertEquals(1_700_000_000_000, model.postTime)
        assertEquals(3, model.priority)
        assertEquals("message", model.category)
    }
}
