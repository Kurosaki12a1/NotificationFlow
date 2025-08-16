package com.kuro.notiflow.presentation.common.view

import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import com.kuro.notiflow.presentation.common.vector.ErrorImage

/**
 * A composable that loads and displays the application icon of a given package.
 *
 * Usage:
 * ```
 * PackageIconImage(
 *     packageName = "com.example.otherapp",
 *     modifier = Modifier.size(48.dp)
 * )
 * ```
 *
 * Behavior:
 * - Tries to resolve the app icon from the installed package via [PackageManager].
 * - If found, the icon is rendered and cached in memory by Coil.
 * - If not found, a fallback vector image [ErrorImage] is displayed.
 *
 * Notes:
 * - Requires the package to be installed on the device; otherwise, the fallback is used.
 * - For Android 11+ (API 30+), the queried package must be declared in
 *   `<queries>` inside `AndroidManifest.xml`.
 * - The `memoryCacheKey` is set to `pkg:<packageName>` to ensure icons are cached
 *   per package and reused efficiently.
 *
 * @param packageName The package name of the target application (e.g. `"com.example.app"`).
 * @param modifier Optional [Modifier] for styling and layout.
 * @param contentScale Defines how the icon should be scaled inside its bounds.
 */
@Composable
fun PackageIconImage(
    packageName: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit
) {
    if (packageName.isEmpty()) return
    val context = LocalContext.current

    val appIcon: Drawable? = remember(packageName) {
        runCatching {
            val pm = context.packageManager
            val packageInfo = pm.getPackageInfo(packageName, PackageManager.GET_META_DATA)
            packageInfo.applicationInfo?.loadIcon(pm)
        }.getOrNull()
    }

    if (appIcon != null) {
        Image(
            painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(context)
                    .data(appIcon)
                    .memoryCacheKey("pkg:$packageName")
                    .build()
            ),
            contentDescription = null,
            modifier = modifier,
            contentScale = contentScale
        )
    } else {
        Image(
            imageVector = ErrorImage,
            contentDescription = null,
            modifier = modifier,
            contentScale = contentScale
        )
    }
}