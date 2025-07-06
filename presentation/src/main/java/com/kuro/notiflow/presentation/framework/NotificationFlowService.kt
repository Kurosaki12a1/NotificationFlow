package com.kuro.notiflow.presentation.framework

import android.app.Notification
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification

class NotificationFlowService : NotificationListenerService() {

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        if (sbn == null) return
        val packageName = sbn.packageName
        val notification = sbn.notification
        val extras = notification.extras

        val title = extras.getString(Notification.EXTRA_TITLE)             // Tiêu đề
        val text = extras.getCharSequence(Notification.EXTRA_TEXT)?.toString() // Nội dung chính
        val subText =
            extras.getCharSequence(Notification.EXTRA_SUB_TEXT)?.toString() // Nội dung phụ
        val bigText =
            extras.getCharSequence(Notification.EXTRA_BIG_TEXT)?.toString() // Nội dung mở rộng
        val appIcon = getAppIcon(this, packageName)

        println("Icon: ${        notification.smallIcon.hashCode()} Title: $title text: $text subText: $subText bigText: $bigText package: ${sbn.packageName} ${appIcon == null} ${sbn.notification.smallIcon == null}")
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
        println("${sbn?.notification}")
    }

    private fun getAppIcon(context: Context, packageName: String): Drawable? {
        return try {
            val pm = context.packageManager
            pm.getApplicationIcon(packageName)
        } catch (e: PackageManager.NameNotFoundException) {
            null
        }
    }
}

