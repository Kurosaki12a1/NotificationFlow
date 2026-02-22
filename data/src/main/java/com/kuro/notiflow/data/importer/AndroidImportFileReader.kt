package com.kuro.notiflow.data.importer

import android.content.Context
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.InputStream
import javax.inject.Inject

class AndroidImportFileReader @Inject constructor(
    @param:ApplicationContext private val context: Context
) : ImportFileReader {
    override fun openInputStream(uriString: String): InputStream? {
        return context.contentResolver.openInputStream(uriString.toUri())
    }
}
