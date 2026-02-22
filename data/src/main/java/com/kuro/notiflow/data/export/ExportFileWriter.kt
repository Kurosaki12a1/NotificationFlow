package com.kuro.notiflow.data.export

import java.io.OutputStream

interface ExportFileWriter {
    fun openOutputStream(uriString: String): OutputStream?
}
