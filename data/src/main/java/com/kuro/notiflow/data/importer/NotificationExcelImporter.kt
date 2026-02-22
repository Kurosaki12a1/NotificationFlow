package com.kuro.notiflow.data.importer

import com.kuro.notiflow.domain.models.notifications.NotificationModel
import org.apache.poi.ss.usermodel.DataFormatter
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.io.InputStream
import javax.inject.Inject

class NotificationExcelImporter @Inject constructor() {
    fun import(inputStream: InputStream): List<NotificationModel> {
        WorkbookFactory.create(inputStream).use { workbook ->
            val sheet = workbook.getSheetAt(0) ?: return emptyList()
            val headerRow = sheet.getRow(sheet.firstRowNum) ?: return emptyList()
            val formatter = DataFormatter()
            val headerSize = headerRow.lastCellNum.toInt().coerceAtLeast(0)
            if (headerSize == 0) return emptyList()
            val header = (0 until headerSize)
                .map { index -> formatter.formatCellValue(headerRow.getCell(index)) }

            val headerMap = NotificationImportMapper.headerMap(header)
            NotificationImportMapper.validateHeader(headerMap)

            val rows = mutableListOf<NotificationModel>()
            val lastRow = sheet.lastRowNum
            for (rowIndex in (sheet.firstRowNum + 1)..lastRow) {
                val row = sheet.getRow(rowIndex)
                if (row == null) continue
                val values = (0 until header.size).map { index ->
                    formatter.formatCellValue(row.getCell(index))
                }
                if (values.all { it.isBlank() }) continue
                rows.add(NotificationImportMapper.toNotification(values, headerMap))
            }
            return rows
        }
    }
}
