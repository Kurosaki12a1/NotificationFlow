package com.kuro.notiflow.data.framework.app

import android.content.Context
import android.content.pm.PackageManager
import com.kuro.notiflow.domain.api.app.AppInfoProvider
import com.kuro.notiflow.domain.models.app.AppSelectionItem
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Android-backed implementation that resolves app labels through the package manager.
 */
class AndroidAppInfoProvider @Inject constructor(
    @param:ApplicationContext private val context: Context
) : AppInfoProvider {
    override fun resolveAppName(packageName: String): String {
        return runCatching {
            val appInfo = context.packageManager.getApplicationInfo(packageName, 0)
            context.packageManager.getApplicationLabel(appInfo).toString()
        }.getOrElse { packageName }
    }

    override suspend fun fetchInstalledApps(): List<AppSelectionItem> {
        val packageManager = context.packageManager
        return packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
            .asSequence()
            .filter { appInfo ->
                packageManager.getLaunchIntentForPackage(appInfo.packageName) != null
            }
            .map { appInfo ->
                AppSelectionItem(
                    packageName = appInfo.packageName,
                    appName = packageManager.getApplicationLabel(appInfo).toString()
                )
            }
            .sortedBy { it.appName.lowercase() }
            .toList()
    }
}
