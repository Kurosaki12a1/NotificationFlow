package com.kuro.notiflow.data.importer

import com.kuro.notiflow.domain.models.notifications.NotificationModel
import java.io.InputStream
import java.nio.charset.Charset
import javax.inject.Inject

class NotificationCsvImporter @Inject constructor() {
    fun import(inputStream: InputStream): List<NotificationModel> {
        val bytes = inputStream.readBytes()
        if (bytes.isEmpty()) return emptyList()
        val text = bytes.toString(Charset.forName("UTF-8"))
        if (text.any { it == '\u0000' }) {
            throw IllegalArgumentException("Binary file is not supported")
        }
        val rows = parseCsv(text)
        if (rows.isEmpty()) return emptyList()
        val header = rows.first()
        val headerMap = NotificationImportMapper.headerMap(header)
        NotificationImportMapper.validateHeader(headerMap)

        return rows.drop(1).mapNotNull { row ->
            if (row.all { it.isBlank() }) return@mapNotNull null
            NotificationImportMapper.toNotification(row, headerMap)
        }
    }

    private fun parseCsv(text: String): List<List<String>> {
        val rows = mutableListOf<MutableList<String>>()
        var row = mutableListOf<String>()
        val cell = StringBuilder()
        var inQuotes = false
        var i = 0
        while (i < text.length) {
            val c = text[i]
            when {
                c == '"' -> {
                    if (inQuotes && i + 1 < text.length && text[i + 1] == '"') {
                        cell.append('"')
                        i++
                    } else {
                        inQuotes = !inQuotes
                    }
                }
                c == ',' && !inQuotes -> {
                    row.add(cell.toString())
                    cell.setLength(0)
                }
                (c == '\n' || c == '\r') && !inQuotes -> {
                    row.add(cell.toString())
                    cell.setLength(0)
                    rows.add(row)
                    row = mutableListOf()
                    if (c == '\r' && i + 1 < text.length && text[i + 1] == '\n') {
                        i++
                    }
                }
                else -> cell.append(c)
            }
            i++
        }
        row.add(cell.toString())
        if (row.any { it.isNotEmpty() }) {
            rows.add(row)
        }
        return rows
    }
}
