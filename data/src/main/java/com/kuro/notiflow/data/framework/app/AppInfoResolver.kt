package com.kuro.notiflow.data.framework.app

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Resolves user-facing app metadata from a package name.
 */
interface AppInfoResolver {
    fun resolveAppName(packageName: String): String
}

/**
 * Android-backed implementation that resolves app labels through the package manager.
 */
class AndroidAppInfoResolver @Inject constructor(
    @param:ApplicationContext private val context: Context
) : AppInfoResolver {
    override fun resolveAppName(packageName: String): String {
        return runCatching {
            val appInfo = context.packageManager.getApplicationInfo(packageName, 0)
            context.packageManager.getApplicationLabel(appInfo).toString()
        }.getOrElse { packageName }
    }
}
