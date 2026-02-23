package com.kuro.notiflow.navigation

object NavigationConstants {
    object Destination {
        const val HOME = "Home"
        const val NOTIFICATIONS = "Notifications"
        const val BOOKMARK = "Bookmark"
        const val SETTINGS = "Settings"
        const val ONBOARDING = "Onboarding"
        const val FILTER = "Filter"
        const val NOTIFICATION_DETAIL = "NotificationDetail"
        const val DATA_MANAGEMENT = "DataManagement"
    }

    object Graph {
        const val HOME_GRAPH = "HomeGraph"
        const val NOTIFICATIONS_GRAPH = "NotificationsGraph"
        const val BOOKMARK_GRAPH = "BookmarkGraph"
        const val SETTINGS_GRAPH = "SettingsGraph"
        const val ONBOARDING_GRAPH = "OnboardingGraph"
    }

    object Delay {
        const val NAVIGATE = 150L
    }
}
