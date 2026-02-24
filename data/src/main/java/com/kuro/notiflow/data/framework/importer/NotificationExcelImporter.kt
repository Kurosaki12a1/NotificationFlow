package com.kuro.notiflow.data.framework.importer

import com.kuro.notiflow.domain.models.notifications.NotificationModel
import org.apache.poi.ss.usermodel.DataFormatter
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.io.InputStream
import javax.inject.Inject

/**
 * An importer for parsing [NotificationModel] data from Excel files (.xls, .xlsx).
 *
 * This class uses the Apache POI library to read the first sheet of an Excel
 * workbook. It maps the columns to the notification model fields using a
 * header-based mapping provided by [NotificationImportMapper].
 *
 * The class is designed to be robust against empty files, empty sheets, and rows
 * that are completely blank. It ensures that cell values are consistently read
 * as strings, regardless of their underlying Excel data type (e.g., Numeric, String).
 */
class NotificationExcelImporter @Inject constructor() : NotificationImporter {

    /**
     * Parses an Excel file from the given [inputStream] into a list of [NotificationModel].
     *
     * The `use` block ensures the underlying workbook and stream are closed automatically,
     * preventing resource leaks.
     *
     * @param inputStream The input stream of the Excel file (.xls or .xlsx).
     * @return A list of successfully parsed notification models. Returns an empty list
     *         if the file, sheet, or header is empty or invalid.
     * @throws org.apache.poi.openxml4j.exceptions.InvalidFormatException If the file is not a valid Excel file.
     * @throws IllegalArgumentException If the file header is missing required columns.
     */
    override fun import(inputStream: InputStream): List<NotificationModel> {
        // WorkbookFactory.create handles both .xls and .xlsx formats.
        // The 'use' statement ensures the workbook is closed automatically.
        WorkbookFactory.create(inputStream).use { workbook ->
            // Assume the data is on the first sheet.
            val sheet = workbook.getSheetAt(0) ?: return emptyList()
            val headerRow = sheet.getRow(sheet.firstRowNum) ?: return emptyList()

            // DataFormatter is crucial for consistently reading cell values as they appear
            // in Excel, preventing issues with numeric or date types being misinterpreted.
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
                    // Using getCell can return null for blank cells, which formatCellValue handles.
                    formatter.formatCellValue(row.getCell(index))
                }
                // Skip rows that contain no data.
                if (values.all { it.isBlank() }) continue
                rows.add(NotificationImportMapper.toNotification(values, headerMap))
            }
            return rows
        }
    }
}
