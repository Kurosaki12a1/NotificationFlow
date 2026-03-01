package com.kuro.notiflow.navigation.model


import com.kuro.notiflow.navigation.NavigationConstants.Destination.BOOKMARK
import com.kuro.notiflow.navigation.NavigationConstants.Destination.BOOKMARK_RULES
import com.kuro.notiflow.navigation.NavigationConstants.Destination.DATA_MANAGEMENT
import com.kuro.notiflow.navigation.NavigationConstants.Destination.FILTER
import com.kuro.notiflow.navigation.NavigationConstants.Destination.HOME
import com.kuro.notiflow.navigation.NavigationConstants.Destination.NOTIFICATION_FILTERS
import com.kuro.notiflow.navigation.NavigationConstants.Destination.NOTIFICATIONS
import com.kuro.notiflow.navigation.NavigationConstants.Destination.NOTIFICATION_DETAIL
import com.kuro.notiflow.navigation.NavigationConstants.Destination.ONBOARDING
import com.kuro.notiflow.navigation.NavigationConstants.Destination.SETTINGS
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    @SerialName(HOME)
    data object Home : Screen()

    @Serializable
    @SerialName(BOOKMARK)
    data object Bookmark : Screen()

    @Serializable
    @SerialName(BOOKMARK_RULES)
    data object BookmarkRules : Screen()

    @Serializable
    @SerialName(NOTIFICATIONS)
    data object Notifications : Screen()

    @Serializable
    @SerialName(SETTINGS)
    data object Settings : Screen()

    @Serializable
    @SerialName(NOTIFICATION_FILTERS)
    data object NotificationFilters : Screen()

    @Serializable
    @SerialName(ONBOARDING)
    data object Onboarding : Screen()

    @Serializable
    @SerialName(FILTER)
    data object Filter : Screen()

    @Serializable
    @SerialName(NOTIFICATION_DETAIL)
    data class NotificationDetail(
        val notificationId: Long
    ) : Screen()

    @Serializable
    @SerialName(DATA_MANAGEMENT)
    data object DataManagement : Screen()

    @Serializable
    data class Statistics(val appName: String) : Screen()
}
