package com.kuro.notiflow.data.framework.importer

import com.kuro.notiflow.domain.models.notifications.NotificationModel
import java.io.InputStream
import java.nio.charset.Charset
import javax.inject.Inject

/**
 * A CSV parser and importer specifically for [NotificationModel] data.
 *
 * This class handles the manual parsing of CSV streams, including support for:
 * - RFC 4180 style quoted fields.
 * - Escape characters within quotes.
 * - Mapping CSV headers to domain models via [NotificationImportMapper].
 *
 * This is an injectable utility intended for use in the data layer.
 */
class NotificationCsvImporter @Inject constructor() : NotificationImporter {

    /**
     * Parses the provided [inputStream] into a list of [NotificationModel].
     *
     * @param inputStream The source stream containing CSV data.
     * @return A list of successfully parsed notification models. Returns an empty list
     *         if the file is empty or contains only a header.
     * @throws IllegalArgumentException If the file is detected as binary (contains null bytes)
     *         or if the CSV header is invalid/missing required columns.
     */
    override fun import(inputStream: InputStream): List<NotificationModel> {
        val bytes = inputStream.readBytes()
        if (bytes.isEmpty()) return emptyList()
        val text = bytes.toString(Charset.forName("UTF-8"))

        // Standard check to prevent processing binary files as text
        if (text.any { it == '\u0000' }) {
            throw IllegalArgumentException("Binary file is not supported")
        }
        val rows = parseCsv(text)
        if (rows.isEmpty()) return emptyList()
        val header = rows.first()
        val headerMap = NotificationImportMapper.headerMap(header)
        NotificationImportMapper.validateHeader(headerMap)

        // Drop the header row and map the remaining data rows
        return rows.drop(1).mapNotNull { row ->
            // Skip rows that are completely blank or contain only whitespace
            if (row.all { it.isBlank() }) return@mapNotNull null
            NotificationImportMapper.toNotification(row, headerMap)
        }
    }

    /**
     * Manual CSV parser implementation to handle complex CSV structures (like line breaks
     * or commas inside quoted strings).
     *
     * @param text The raw CSV string content.
     * @return A list of rows, where each row is a list of column strings.
     */
    private fun parseCsv(text: String): List<List<String>> {
        val rows = mutableListOf<MutableList<String>>()
        var row = mutableListOf<String>()
        val cell = StringBuilder()
        var inQuotes = false
        var i = 0
        while (i < text.length) {
            val c = text[i]
            when {
                // Handle quotes and escaped quotes ("")
                c == '"' -> {
                    if (inQuotes && i + 1 < text.length && text[i + 1] == '"') {
                        cell.append('"')
                        i++
                    } else {
                        inQuotes = !inQuotes
                    }
                }
                // Handle column delimiters
                c == ',' && !inQuotes -> {
                    row.add(cell.toString())
                    cell.setLength(0)
                }
                // Handle row delimiters (LF or CRLF)
                (c == '\n' || c == '\r') && !inQuotes -> {
                    row.add(cell.toString())
                    cell.setLength(0)
                    rows.add(row)
                    row = mutableListOf()
                    // Peek ahead for CRLF pair
                    if (c == '\r' && i + 1 < text.length && text[i + 1] == '\n') {
                        i++
                    }
                }
                // Standard character
                else -> cell.append(c)
            }
            i++
        }
        // Add the final cell and row if the file doesn't end in a newline
        row.add(cell.toString())
        if (row.any { it.isNotEmpty() }) {
            rows.add(row)
        }
        return rows
    }
}
