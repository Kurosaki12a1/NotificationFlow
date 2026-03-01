package com.kuro.notiflow.domain.api.app

import com.kuro.notiflow.domain.models.app.AppSelectionItem

interface AppInfoProvider {
    fun resolveAppName(packageName: String): String
    suspend fun fetchInstalledApps(): List<AppSelectionItem>
}
