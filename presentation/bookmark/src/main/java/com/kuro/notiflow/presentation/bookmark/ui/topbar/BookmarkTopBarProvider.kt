package com.kuro.notiflow.presentation.bookmark.ui.topbar

import androidx.compose.runtime.Composable
import com.kuro.notiflow.domain.utils.AppLog
import com.kuro.notiflow.navigation.NavigationConstants.Destination.BOOKMARK
import com.kuro.notiflow.navigation.model.Screen
import com.kuro.notiflow.presentation.common.AppScope
import com.kuro.notiflow.presentation.common.topbar.TopBarProvider
import javax.inject.Inject

class BookmarkTopBarProvider @Inject constructor() : TopBarProvider {
    override val route: String = BOOKMARK

    @Composable
    override fun AppScope.Render() {
        BookmarkTopAppBar(
            onRulesClick = {
                AppLog.d(TAG, "openBookmarkRules")
                navigateTo(Screen.BookmarkRules)
            }
        )
    }
}

private const val TAG = "BookmarkTopBarProvider"
