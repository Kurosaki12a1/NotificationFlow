package com.kuro.notiflow.domain

object Constants {

    object App {
        const val DEVELOPER = "Huynh Minh Thinh"
        const val LICENCE = "Apache Licence v2.0"
        const val PERMISSION_TAG = "Notification_Permission"
        const val GITHUB_URI = "https://github.com/Kurosaki12a1/NotificationFlow"
        const val ISSUES_URI = "https://github.com/Kurosaki12a1/NotificationFlow/issues"
    }

    object Delay {
        const val MARQUEE_LOADING_TEXT = 1500
        const val NAVIGATION_FLOW = 150L
    }

    object Destination {
        const val HOME = "Home"
        const val NOTIFICATIONS = "Notifications"
        const val SETTINGS = "Settings"
        const val FILTER = "Filter"
        const val NOTIFICATION_DETAIL = "NotificationDetail"
    }

    object Home {
        const val OVERVIEW_KEY = "overview"
        const val STATISTIC_KEY = "statistic"
        const val RECENT_NOTIFICATION = "recent_notifications"
    }
}