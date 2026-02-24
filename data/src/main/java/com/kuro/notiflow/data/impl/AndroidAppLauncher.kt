package com.kuro.notiflow.data.impl

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.kuro.notiflow.domain.api.app.AppLauncher
import com.kuro.notiflow.domain.utils.AppLog
import com.kuro.notiflow.domain.models.app.AppLaunchResult
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Android-specific implementation of [AppLauncher].
 *
 * This class handles navigation to external applications or their system settings
 * (App Info) pages. It uses the [Context] and [PackageManager] to resolve intents.
 */
class AndroidAppLauncher @Inject constructor(
    @param:ApplicationContext private val context: Context
) : AppLauncher {

    /**
     * Attempts to open the application associated with the given [packageName].
     *
     * The method first tries to resolve a launch intent for the package. If no launch
     * intent exists (e.g., the app has no launcher activity), it falls back to
     * opening the system's App Info page for that package.
     *
     * @param packageName The unique application identifier (e.g., "com.example.app").
     * @return An [AppLaunchResult] indicating whether the app was opened,
     *         the info page was opened, or the operation failed.
     */
    override fun openApp(packageName: String): AppLaunchResult {
        val launchIntent = context.packageManager.getLaunchIntentForPackage(packageName)
        if (launchIntent != null) {
            return if (startActivitySafely(launchIntent)) {
                AppLaunchResult.OPENED
            } else {
                openAppInfo(packageName)
            }
        }
        // Fallback for apps without a launcher activity.
        return openAppInfo(packageName)
    }

    /**
     * Opens the Android System Settings "App Info" page for the specified [packageName].
     *
     * @param packageName The package name of the app whose settings should be opened.
     * @return [AppLaunchResult.OPENED_APP_INFO] if successful, [AppLaunchResult.FAILED] otherwise.
     */
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

    /**
     * Executes [context.startActivity] within a try-catch block to handle common
     * Android intent resolution errors.
     *
     * @param intent The intent to be executed.
     * @return `true` if the activity started successfully, `false` if an error occurred.
     */
    private fun startActivitySafely(intent: Intent): Boolean {
        return try {
            // Must use NEW_TASK when starting an activity from an Application Context.
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            true
        } catch (ex: ActivityNotFoundException) {
            AppLog.e(TAG, "Activity not found", ex)
            false
        } catch (ex: SecurityException) {
            // Can happen if the intent requires permissions the caller doesn't have.
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
