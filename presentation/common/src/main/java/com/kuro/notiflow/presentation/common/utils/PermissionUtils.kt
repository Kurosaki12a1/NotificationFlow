package com.kuro.notiflow.presentation.common.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
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
}
