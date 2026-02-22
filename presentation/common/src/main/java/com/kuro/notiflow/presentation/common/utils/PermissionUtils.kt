package com.kuro.notiflow.presentation.common.utils

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.core.content.ContextCompat

object PermissionUtils {
    fun needsStoragePermission(): Boolean = Build.VERSION.SDK_INT < Build.VERSION_CODES.Q

    fun storagePermissions(): Array<String> {
        return if (needsStoragePermission()) {
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        } else {
            emptyArray()
        }
    }

    fun needsPostNotificationsPermission(): Boolean =
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

    fun postNotificationsPermissions(): Array<String> {
        return if (needsPostNotificationsPermission()) {
            arrayOf(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            emptyArray()
        }
    }

    fun hasStoragePermission(context: Context): Boolean {
        return hasPermissions(context, storagePermissions())
    }

    fun hasPermissions(context: Context, permissions: Array<String>): Boolean {
        if (permissions.isEmpty()) return true
        return permissions.all { permission ->
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        }
    }

    fun hasPostNotificationsPermission(context: Context): Boolean {
        return hasPermissions(context, postNotificationsPermissions())
    }

    fun isNotificationListenerEnabled(context: Context): Boolean {
        val pkgName = context.packageName
        val enabledListeners = Settings.Secure.getString(
            context.contentResolver,
            "enabled_notification_listeners"
        )
        return enabledListeners?.contains(pkgName) == true
    }

    fun notificationListenerSettingsIntent(): Intent {
        return Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
    }
}
