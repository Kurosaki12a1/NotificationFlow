package com.kuro.notiflow.presentation.common.navigation

import com.kuro.notiflow.domain.Constants
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    @SerialName(Constants.Destination.HOME)
    data object Home : Screen()

    @Serializable
    @SerialName(Constants.Destination.NOTIFICATIONS)
    data object Notifications : Screen()

    @Serializable
    @SerialName(Constants.Destination.SETTINGS)
    data object Settings : Screen()

    @Serializable
    @SerialName(Constants.Destination.FILTER)
    data object Filter : Screen()

    @Serializable
    data class Statistics(val appName: String) : Screen()
}