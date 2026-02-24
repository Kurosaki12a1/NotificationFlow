package com.kuro.notiflow.data.framework.exporter

import java.io.OutputStream

interface ExportFileWriter {
    fun openOutputStream(uriString: String): OutputStream?
}
