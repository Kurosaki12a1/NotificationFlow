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

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var features: Set<@JvmSuppressWildcards FeatureNav>

    @Inject
    lateinit var topBarProviders: Set<@JvmSuppressWildcards TopBarProvider>

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
