package com.kuro.notiflow.navigation.model


import com.kuro.notiflow.navigation.NavigationConstants.Graph.HOME_GRAPH
import com.kuro.notiflow.navigation.NavigationConstants.Graph.NOTIFICATIONS_GRAPH
import com.kuro.notiflow.navigation.NavigationConstants.Graph.SETTINGS_GRAPH
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed class Graph {
    @Serializable
    @SerialName(HOME_GRAPH)
    data object HomeGraph : Graph()

    @Serializable
    @SerialName(NOTIFICATIONS_GRAPH)
    data object NotificationsGraph : Graph()

    @Serializable
    @SerialName(SETTINGS_GRAPH)
    data object SettingsGraph : Graph()
}