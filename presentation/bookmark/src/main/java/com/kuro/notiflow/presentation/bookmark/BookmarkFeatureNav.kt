package com.kuro.notiflow.presentation.bookmark

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.kuro.notiflow.navigation.model.Graph
import com.kuro.notiflow.navigation.model.Screen
import com.kuro.notiflow.navigation.utils.FeatureNav
import com.kuro.notiflow.presentation.bookmark.ui.BookmarkScreen
import jakarta.inject.Inject

class BookmarkFeatureNav @Inject constructor() : FeatureNav {

    override fun register(builder: NavGraphBuilder) {
        builder.navigation<Graph.BookmarkGraph>(
            startDestination = Screen.Bookmark
        ) {
            composable<Screen.Bookmark> {
                BookmarkScreen()
            }
        }
    }
}
