package com.kuro.notiflow.presentation.common

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.kuro.notiflow.navigation.utils.FeatureNav
import com.kuro.notiflow.presentation.common.theme.NotificationFlowTheme
import com.kuro.notiflow.presentation.common.topbar.TopBarProvider
import com.kuro.notiflow.presentation.common.ui.dialog.DialogController
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 *
 * This activity is responsible for:
 * 1. Initializing the dependency injection container via [AndroidEntryPoint].
 * 2. Setting up the edge-to-edge system UI configuration.
 * 3. Providing the root Compose entry point ([App]) with injected features and controllers.
 *
 * The activity uses Hilt multi-bindings to collect [FeatureNav] and [TopBarProvider]
 * implementations from various modules, allowing for a decoupled and scalable architecture.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    /**
     * Set of navigation handlers for all features in the app.
     * Injected via Hilt multi-binding.
     */
    @Inject
    lateinit var features: Set<@JvmSuppressWildcards FeatureNav>

    /**
     * Set of providers that define the TopBar behavior for different screens.
     * Injected via Hilt multi-binding.
     */
    @Inject
    lateinit var topBarProviders: Set<@JvmSuppressWildcards TopBarProvider>

    /**
     * Global controller for managing dialog visibility and content across the app.
     */
    @Inject
    lateinit var dialogController: DialogController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NotificationFlowTheme {
                App(
                    features = features,
                    topBarProviders = topBarProviders,
                    dialogController = dialogController
                )
            }
        }
    }
}
