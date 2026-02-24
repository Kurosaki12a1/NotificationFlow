package com.kuro.notiflow.data.framework.importer

import android.content.Context
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.InputStream
import javax.inject.Inject

/**
 * Android-specific implementation of [ImportFileReader].
 *
 * This class leverages the [Context] to resolve URIs provided by the Android system
 * (such as those from the Storage Access Framework) into [InputStream] objects
 * that can be processed by data parsers (e.g., Apache POI for Excel files).
 */
class AndroidImportFileReader @Inject constructor(
    @param:ApplicationContext private val context: Context
) : ImportFileReader {

    /**
     * Opens an [InputStream] for the given [uriString].
     *
     * @param uriString A string representation of the URI to be opened.
     * @return An [InputStream] for the data represented by the URI, or `null` if the
     *         content provider could not be reached or the URI is invalid.
     * @throws IllegalArgumentException If the [uriString] cannot be parsed as a URI.
     * @throws SecurityException If the app does not have permission to access the URI.
     */
    override fun openInputStream(uriString: String): InputStream? {
        return context.contentResolver.openInputStream(uriString.toUri())
    }
}
