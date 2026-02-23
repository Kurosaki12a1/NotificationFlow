package com.kuro.notiflow.data.impl

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.kuro.notiflow.domain.api.app.AppLauncher
import com.kuro.notiflow.domain.logger.AppLog
import com.kuro.notiflow.domain.models.app.AppLaunchResult
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AndroidAppLauncher @Inject constructor(
    @param:ApplicationContext private val context: Context
) : AppLauncher {
    override fun openApp(packageName: String): AppLaunchResult {
        val launchIntent = context.packageManager.getLaunchIntentForPackage(packageName)
        if (launchIntent != null) {
            return if (startActivitySafely(launchIntent)) {
                AppLaunchResult.OPENED
            } else {
                openAppInfo(packageName)
            }
        }
        return openAppInfo(packageName)
    }

    private fun openAppInfo(packageName: String): AppLaunchResult {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null)
        }
        return if (startActivitySafely(intent)) {
            AppLaunchResult.OPENED_APP_INFO
        } else {
            AppLaunchResult.FAILED
        }
    }

    private fun startActivitySafely(intent: Intent): Boolean {
        return try {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            true
        } catch (ex: ActivityNotFoundException) {
            AppLog.e(TAG, "Activity not found", ex)
            false
        } catch (ex: SecurityException) {
            AppLog.e(TAG, "Security exception", ex)
            false
        } catch (ex: Exception) {
            AppLog.e(TAG, "Failed to open app", ex)
            false
        }
    }

    companion object {
        private const val TAG = "AndroidAppLauncher"
    }
}
