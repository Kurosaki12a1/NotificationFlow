package com.kuro.notiflow.presentation.common.navigation

import androidx.navigation3.runtime.NavKey
import com.kuro.notiflow.domain.Constants
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed class Screen : NavKey {
    @Serializable
    @SerialName(Constants.Destination.HOME)
    data object Home : Screen()

    @Serializable
    @SerialName(Constants.Destination.NOTIFICATIONS)
    data object Notifications : Screen()

    @Serializable
    @SerialName(Constants.Destination.SETTINGS)
    data object Settings : Screen()
}