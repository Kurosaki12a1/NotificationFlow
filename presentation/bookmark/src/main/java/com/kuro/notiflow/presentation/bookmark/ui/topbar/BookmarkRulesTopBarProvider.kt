package com.kuro.notiflow.presentation.bookmark.ui.topbar

import androidx.compose.runtime.Composable
import com.kuro.notiflow.domain.utils.AppLog
import com.kuro.notiflow.navigation.NavigationConstants.Destination.BOOKMARK_RULES
import com.kuro.notiflow.presentation.common.AppScope
import com.kuro.notiflow.presentation.common.topbar.TopBarProvider
import javax.inject.Inject

class BookmarkRulesTopBarProvider @Inject constructor() : TopBarProvider {
    override val route: String = BOOKMARK_RULES

    @Composable
    override fun AppScope.Render() {
        BookmarkRulesTopAppBar(
            onBackClick = {
                AppLog.d(TAG, "back")
                popBackStack()
            }
        )
    }
}

private const val TAG = "BookmarkRulesTopBarProvider"
