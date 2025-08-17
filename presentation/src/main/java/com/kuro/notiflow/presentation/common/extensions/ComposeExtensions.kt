package com.kuro.notiflow.presentation.common.extensions

import androidx.compose.foundation.basicMarquee
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import com.kuro.notiflow.domain.Constants.Delay.MARQUEE_LOADING_TEXT

/**
 * This function creates a [Modifier] that enables scrolling text for a Composable element.
 *
 * The `scrollText` modifier uses the `basicMarquee` function to create a marquee effect, which is
 * typically used to display text that scrolls continuously across the screen. This can be useful for
 * displaying long pieces of text in a limited space or for drawing attention to important information.
 *
 * The `iterations` parameter is set to `Int.MAX_VALUE`, which means the scrolling effect will repeat
 * indefinitely. The `repeatDelayMillis` parameter is set to `MARQUEE_LOADING_TEXT`, which likely
 * represents the delay between each iteration of the scrolling effect.
 *
 * Usage example:
 * ```
 * @Composable
 * fun ScrollingText() {
 *     Text(
 *         text = "This is a scrolling text example",
 *         modifier = Modifier.scrollText()
 *     )
 * }
 * ```
 *
 * In this example, the `Text` Composable will have its text scroll continuously across the screen.
 */
@Composable
fun Modifier.scrollText(): Modifier = this.basicMarquee(
    iterations = Int.MAX_VALUE,
    repeatDelayMillis = MARQUEE_LOADING_TEXT
)

/**
 * Returns the current route string from the [NavBackStackEntry], or `null` if the entry is null.
 *
 * @receiver NavBackStackEntry? The back stack entry from which to extract the current route.
 * @return The route string, or `null` if the entry is null.
 */
fun NavBackStackEntry?.getCurrentRoute(): String? {
    if (this == null) return null
    return try {
        destination.route?.substringBefore("/") // Remove query part
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
/**
 * Updates the current [MutableState] value by applying the given transformation function.
 *
 * @param updateFunc A function that takes the current value and returns the updated value.
 */
fun <T> MutableState<T>.update(updateFunc: (T) -> T) {
    this.value = updateFunc(this.value)
}