package com.kuro.notiflow.data.importer

import java.io.InputStream

interface ImportFileReader {
    fun openInputStream(uriString: String): InputStream?
}
