package com.kuro.notiflow.domain.use_case

import com.kuro.notiflow.domain.api.app.AppLauncher
import com.kuro.notiflow.domain.models.app.AppLaunchResult
import javax.inject.Inject

class OpenAppUseCase @Inject constructor(
    private val appLauncher: AppLauncher
) {
    operator fun invoke(packageName: String): AppLaunchResult {
        return appLauncher.openApp(packageName)
    }
}
