package com.kuro.notiflow.data.framework.exporter

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import androidx.core.net.toUri

class AndroidExportFileWriter @Inject constructor(
    @param:ApplicationContext private val context: Context
) : ExportFileWriter {
    override fun openOutputStream(uriString: String) =
        context.contentResolver.openOutputStream(uriString.toUri())
}
