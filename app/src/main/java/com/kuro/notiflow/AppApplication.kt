package com.kuro.notiflow

import android.app.Application
import com.kuro.notiflow.domain.logger.AppLog
import com.kuro.notiflow.domain.logger.Logger
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class AppApplication : Application() {
    @Inject
    lateinit var logger: Logger

    override fun onCreate() {
        super.onCreate()
        AppLog.setLogger(logger)
    }
}
