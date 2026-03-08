package com.kuro.notiflow.presentation.common.ui.local

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource

/**
 * Tracks bottom bar visibility based on user-driven vertical scroll.
 *
 * This holder uses hysteresis: showing the bar requires a larger upward scroll than hiding it.
 * The intent is to avoid flicker and accidental re-show when users make short upward drags near
 * the end of a list.
 */
class BottomBarScrollVisibilityHolder {
    var isVisible: Boolean by mutableStateOf(true)
        private set

    private var accumulatedDy: Float = 0f
    // Runtime height from the rendered bottom bar. Used to scale thresholds across devices.
    private var bottomBarHeightPx: Float = DEFAULT_BOTTOM_BAR_HEIGHT_PX

    val nestedScrollConnection: NestedScrollConnection = object : NestedScrollConnection {
        override fun onPostScroll(
            consumed: Offset,
            available: Offset,
            source: NestedScrollSource
        ): Offset {
            if (source != NestedScrollSource.Drag) return Offset.Zero
            val deltaY = consumed.y
            if (deltaY == 0f) return Offset.Zero

            when {
                deltaY < 0f -> { // scrolling down
                    if (accumulatedDy > 0f) accumulatedDy = 0f
                    accumulatedDy += deltaY
                    if (isVisible && accumulatedDy <= -hideThresholdPx()) {
                        isVisible = false
                        accumulatedDy = 0f
                    }
                }
                deltaY > 0f -> { // scrolling up
                    if (accumulatedDy < 0f) accumulatedDy = 0f
                    accumulatedDy += deltaY
                    if (!isVisible && accumulatedDy >= showThresholdPx()) {
                        isVisible = true
                        accumulatedDy = 0f
                    }
                }
            }
            return Offset.Zero
        }
    }

    fun show() {
        isVisible = true
        accumulatedDy = 0f
    }

    /**
     * Updates the measured bottom bar height so scroll thresholds stay proportional to UI size.
     */
    fun updateBottomBarHeight(heightPx: Int) {
        if (heightPx > 0) {
            bottomBarHeightPx = heightPx.toFloat()
        }
    }

    private fun hideThresholdPx(): Float = bottomBarHeightPx * HIDE_THRESHOLD_RATIO

    private fun showThresholdPx(): Float = bottomBarHeightPx * SHOW_THRESHOLD_RATIO
}

val LocalBottomBarScrollVisibility = staticCompositionLocalOf<BottomBarScrollVisibilityHolder> {
    error("No BottomBarScrollVisibilityHolder provided")
}

// Fallback used before the first layout pass provides real bottom bar height.
private const val DEFAULT_BOTTOM_BAR_HEIGHT_PX = 80f
// Hide sooner on downward drag to maximize content area quickly.
private const val HIDE_THRESHOLD_RATIO = 0.5f
// Show later on upward drag to reduce accidental overlap with bottom content.
private const val SHOW_THRESHOLD_RATIO = 1f
