package com.kuro.notiflow.data.impl

import com.kuro.notiflow.data.importer.ImportFileReader
import com.kuro.notiflow.data.importer.NotificationCsvImporter
import com.kuro.notiflow.data.importer.NotificationExcelImporter
import com.kuro.notiflow.domain.models.notifications.NotificationModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class NotificationImportRepositoryImplTest {

    @Test
    fun `uses csv importer when file is not excel`() = runBlocking {
        val csvImporter = mockk<NotificationCsvImporter>()
        val excelImporter = mockk<NotificationExcelImporter>()
        val fileReader = mockk<ImportFileReader>()
        val repository = NotificationImportRepositoryImpl(csvImporter, excelImporter, fileReader)
        val csvBytes = "id,packageName,postTime,priority,category\n1,com.test,1700000000000,3,message".toByteArray()

        every { fileReader.openInputStream("content://test") } returns ByteArrayInputStream(csvBytes)
        every { csvImporter.import(any()) } returns emptyList()

        val result = repository.importNotifications("content://test")

        assertTrue(result.isSuccess)
        verify(exactly = 1) { csvImporter.import(any()) }
        verify(exactly = 0) { excelImporter.import(any()) }
    }

    @Test
    fun `uses excel importer when file is xlsx`() = runBlocking {
        val csvImporter = mockk<NotificationCsvImporter>()
        val excelImporter = mockk<NotificationExcelImporter>()
        val fileReader = mockk<ImportFileReader>()
        val repository = NotificationImportRepositoryImpl(csvImporter, excelImporter, fileReader)

        val workbook = XSSFWorkbook()
        val out = ByteArrayOutputStream()
        workbook.use { it.write(out) }
        val xlsxBytes = out.toByteArray()

        every { fileReader.openInputStream("content://test") } returns ByteArrayInputStream(xlsxBytes)
        every { excelImporter.import(any()) } returns listOf(
            NotificationModel(
                id = 1,
                packageName = "com.test",
                title = null,
                text = null,
                subText = null,
                bigText = null,
                summaryText = null,
                infoText = null,
                textLines = null,
                postTime = 1_700_000_000_000,
                priority = 3,
                category = "message",
                smallIconResId = null,
                iconBase64 = null,
                groupKey = null,
                channelId = null,
                isRead = false,
                isBookmarked = false
            )
        )

        val result = repository.importNotifications("content://test")

        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrNull()?.size)
        verify(exactly = 1) { excelImporter.import(any()) }
        verify(exactly = 0) { csvImporter.import(any()) }
    }

    @Test
    fun `returns failure when input stream is null`() = runBlocking {
        val csvImporter = mockk<NotificationCsvImporter>()
        val excelImporter = mockk<NotificationExcelImporter>()
        val fileReader = mockk<ImportFileReader>()
        val repository = NotificationImportRepositoryImpl(csvImporter, excelImporter, fileReader)

        every { fileReader.openInputStream("content://test") } returns null

        val result = repository.importNotifications("content://test")

        assertTrue(result.isFailure)
    }
}
