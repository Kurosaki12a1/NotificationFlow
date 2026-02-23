package com.kuro.notiflow.domain.api.app

import com.kuro.notiflow.domain.models.app.AppLaunchResult

interface AppLauncher {
    fun openApp(packageName: String): AppLaunchResult
}
