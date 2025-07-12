package com.kuro.notiflow.presentation.framework

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.kuro.notiflow.domain.api.notifications.NotificationRepository
import com.kuro.notiflow.domain.models.notifications.NotificationModel
import com.kuro.notiflow.presentation.common.extensions.charSequenceToString
import com.kuro.notiflow.presentation.common.extensions.string
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NotificationFlowService : NotificationListenerService() {

    @Inject
    lateinit var repository: NotificationRepository

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        if (sbn == null) return
        val extras = sbn.notification.extras
        val model = NotificationModel(
            packageName = sbn.packageName,
            title = extras.string(Notification.EXTRA_TITLE),
            text = extras.charSequenceToString(Notification.EXTRA_TEXT),
            subText = extras.charSequenceToString(Notification.EXTRA_SUB_TEXT),
            bigText = extras.charSequenceToString(Notification.EXTRA_BIG_TEXT),
            postTime = sbn.postTime,
            smallIconResId = sbn.notification.smallIcon.resId,
            iconBase64 = null,
            groupKey = sbn.groupKey,
            channelId = sbn.notification.channelId,
            isRead = false
        )
        scope.launch { repository.addNotification(model) }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}

