package com.kuro.notiflow.presentation.common.framework

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import androidx.annotation.StringRes
import com.kuro.notiflow.domain.api.notifications.NotificationRepository
import com.kuro.notiflow.domain.models.notifications.NotificationModel
import com.kuro.notiflow.domain.utils.AppLog
import com.kuro.notiflow.presentation.common.extensions.charSequenceToString
import com.kuro.notiflow.presentation.common.extensions.string
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import com.kuro.notiflow.presentation.common.R as CommonR

@AndroidEntryPoint
class NotificationFlowService : NotificationListenerService() {

    @Inject
    lateinit var repository: NotificationRepository

    companion object {
        private const val TAG = "NotificationFlowService"
    }
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onListenerConnected() {
        super.onListenerConnected()
        AppLog.i(TAG, "Notification listener connected")
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?, rankingMap: RankingMap?) {
        super.onNotificationPosted(sbn, rankingMap)
        if (sbn == null) return
//        val isPermanent = (sbn.notification.flags and Notification.FLAG_ONGOING_EVENT) != 0
//        // Don't need to add permanent notifications
//        if (isPermanent) return
        val priorityNotification = Ranking()
        rankingMap?.getRanking(sbn.key, priorityNotification)
        val extras = sbn.notification.extras
        val model = NotificationModel(
            packageName = sbn.packageName,
            title = extras.string(Notification.EXTRA_TITLE),
            text = extras.charSequenceToString(Notification.EXTRA_TEXT),
            subText = extras.charSequenceToString(Notification.EXTRA_SUB_TEXT),
            bigText = extras.charSequenceToString(Notification.EXTRA_BIG_TEXT),
            summaryText = extras.charSequenceToString(Notification.EXTRA_SUMMARY_TEXT),
            infoText = extras.charSequenceToString(Notification.EXTRA_INFO_TEXT),
            textLines = extras.getCharSequenceArray(Notification.EXTRA_TEXT_LINES)
                ?.joinToString("\n")?.map { it.toString() },
            postTime = sbn.postTime,
            priority = priorityNotification.importance,
            category = getString(mapSystemCategory(sbn.notification.category)),
            smallIconResId = sbn.notification.smallIcon.resId,
            iconBase64 = null,
            groupKey = sbn.groupKey,
            channelId = sbn.notification.channelId,
            isRead = false,
            isBookmarked = false
        )
        scope.launch { repository.addNotification(model) }
    }

    override fun onDestroy() {
        super.onDestroy()
        AppLog.i(TAG, "Notification listener destroyed")
        scope.cancel()
    }

    @StringRes
    private fun mapSystemCategory(category: String?): Int = when (category) {
        Notification.CATEGORY_CALL -> CommonR.string.notification_category_call
        Notification.CATEGORY_MESSAGE -> CommonR.string.notification_category_message
        Notification.CATEGORY_EMAIL -> CommonR.string.notification_category_email
        Notification.CATEGORY_EVENT -> CommonR.string.notification_category_event
        Notification.CATEGORY_REMINDER -> CommonR.string.notification_category_reminder
        Notification.CATEGORY_ALARM -> CommonR.string.notification_category_alarm
        Notification.CATEGORY_NAVIGATION -> CommonR.string.notification_category_navigation
        Notification.CATEGORY_TRANSPORT -> CommonR.string.notification_category_transport
        Notification.CATEGORY_PROGRESS -> CommonR.string.notification_category_progress
        Notification.CATEGORY_PROMO -> CommonR.string.notification_category_promo
        Notification.CATEGORY_SERVICE -> CommonR.string.notification_category_service
        Notification.CATEGORY_SOCIAL -> CommonR.string.notification_category_social
        Notification.CATEGORY_ERROR -> CommonR.string.notification_category_error
        Notification.CATEGORY_SYSTEM -> CommonR.string.notification_category_system
        Notification.CATEGORY_RECOMMENDATION -> CommonR.string.notification_category_recommendation
        Notification.CATEGORY_STATUS -> CommonR.string.notification_category_status
        else -> CommonR.string.notification_category_other
    }
}
