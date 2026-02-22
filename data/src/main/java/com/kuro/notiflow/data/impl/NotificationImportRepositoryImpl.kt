package com.kuro.notiflow.data.impl

import com.kuro.notiflow.data.importer.ImportFileReader
import com.kuro.notiflow.data.importer.NotificationCsvImporter
import com.kuro.notiflow.data.importer.NotificationExcelImporter
import com.kuro.notiflow.domain.api.importer.NotificationImportRepository
import com.kuro.notiflow.domain.models.notifications.NotificationModel
import com.kuro.notiflow.domain.utils.wrap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.poi.poifs.filesystem.FileMagic
import java.io.ByteArrayInputStream
import javax.inject.Inject

class NotificationImportRepositoryImpl @Inject constructor(
    private val csvImporter: NotificationCsvImporter,
    private val excelImporter: NotificationExcelImporter,
    private val fileReader: ImportFileReader
) : NotificationImportRepository {
    override suspend fun importNotifications(uriString: String): Result<List<NotificationModel>> =
        withContext(Dispatchers.IO) {
            wrap {
                fileReader.openInputStream(uriString)?.use { input ->
                    val bytes = input.readBytes()
                    if (bytes.isEmpty()) return@wrap emptyList()
                    val magic = FileMagic.valueOf(ByteArrayInputStream(bytes))
                    when (magic) {
                        FileMagic.OLE2, FileMagic.OOXML ->
                            excelImporter.import(ByteArrayInputStream(bytes))
                        else -> csvImporter.import(ByteArrayInputStream(bytes))
                    }
                } ?: error("Failed to open import input stream")
            }
        }
}
