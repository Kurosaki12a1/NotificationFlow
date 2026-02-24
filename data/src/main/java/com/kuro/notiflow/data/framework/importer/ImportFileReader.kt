package com.kuro.notiflow.data.framework.importer

import java.io.InputStream

interface ImportFileReader {
    fun openInputStream(uriString: String): InputStream?
}
