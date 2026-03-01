package com.kuro.notiflow.domain.use_case

import com.kuro.notiflow.domain.api.app.AppInfoProvider
import com.kuro.notiflow.domain.models.app.AppSelectionItem
import javax.inject.Inject

class FetchInstalledAppsUseCase @Inject constructor(
    private val appInfoProvider: AppInfoProvider
) {
    suspend operator fun invoke(): List<AppSelectionItem> {
        return appInfoProvider.fetchInstalledApps()
    }
}
