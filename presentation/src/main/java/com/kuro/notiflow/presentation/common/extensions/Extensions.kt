package com.kuro.notiflow.presentation.common.extensions

import android.os.Bundle

/**
 * Returns the value associated with the given key as a String if the key exists
 * and the value is not null. Otherwise, returns null.
 *
 * @param key The key for the desired value.
 * @return The string value if present and non-null, or empty String otherwise.
 */
fun Bundle.string(key: String): String {
    return if (containsKey(key)) getString(key) ?: "" else ""
}

/**
 * Returns the value associated with the given key as a CharSequence converted to String
 * if the key exists and the value is not null. Otherwise, returns null.
 *
 * @param key The key for the desired value.
 * @return The CharSequence value as a string if present and non-null, or empty String otherwise.
 */
fun Bundle.charSequenceToString(key: String): String {
    return if (containsKey(key)) getCharSequence(key)?.toString() ?: "" else ""
}