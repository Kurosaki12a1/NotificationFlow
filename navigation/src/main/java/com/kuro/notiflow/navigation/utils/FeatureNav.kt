package com.kuro.notiflow.navigation.utils

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController

interface FeatureNav {
    fun register(
        builder: NavGraphBuilder,
        navController: NavHostController
    )
}