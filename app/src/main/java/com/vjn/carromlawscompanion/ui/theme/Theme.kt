package com.vjn.carromlawscompanion.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.vjn.carromlawscompanion.LocalFontScale
import com.vjn.carromlawscompanion.SettingsManager

private val DarkColorScheme = darkColorScheme(
    primary = CarromBrownDark,
    onPrimary = CarromDarkBrown,
    primaryContainer = CarromContainerDark,
    onPrimaryContainer = CarromOnDark,
    secondary = CarromGold,
    onSecondary = CarromDarkBrown,
    tertiary = CarromQueenRedDark,
    onTertiary = CarromOnDark,
    background = CarromBackgroundDark,
    onBackground = CarromOnDark,
    surface = CarromBackgroundDark,
    onSurface = CarromOnDark,
    surfaceVariant = CarromContainerDark,
    onSurfaceVariant = CarromOnDark,
    error = CarromQueenRedDark,
    errorContainer = CarromQueenRedDark,
    onErrorContainer = CarromOnDark
)

private val LightColorScheme = lightColorScheme(
    primary = CarromBrown,
    onPrimary = CarromCream,
    primaryContainer = CarromLightBrown,
    onPrimaryContainer = CarromDarkBrown,
    secondary = CarromGold,
    onSecondary = CarromCream,
    tertiary = CarromQueenRed,
    onTertiary = CarromCream,
    background = CarromCream,
    onBackground = CarromDarkBrown,
    surface = Color(0xFFFFFFFF),
    onSurface = CarromDarkBrown,
    surfaceVariant = CarromLightBrown,
    onSurfaceVariant = CarromDarkBrown,
    error = CarromQueenRed,
    errorContainer = Color(0xFFFFCDD2),
    onErrorContainer = Color(0xFFB71C1C)
)

@Composable
fun CarromLawsCompanionTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val settingsManager = remember { SettingsManager(context) }
    val fontScale = settingsManager.getFontSize().scale

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // Provide the font scale to all child screens via CompositionLocal
    CompositionLocalProvider(LocalFontScale provides fontScale) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = scaledTypography(fontScale),
            content = content
        )
    }
}
@Composable
private fun scaledTypography(scale: Float): androidx.compose.material3.Typography {
    val base = androidx.compose.material3.Typography()
    return androidx.compose.material3.Typography(
        displayLarge = base.displayLarge.copy(fontSize = base.displayLarge.fontSize.times(scale)),
        displayMedium = base.displayMedium.copy(fontSize = base.displayMedium.fontSize.times(scale)),
        displaySmall = base.displaySmall.copy(fontSize = base.displaySmall.fontSize.times(scale)),
        headlineLarge = base.headlineLarge.copy(fontSize = base.headlineLarge.fontSize.times(scale)),
        headlineMedium = base.headlineMedium.copy(fontSize = base.headlineMedium.fontSize.times(scale)),
        headlineSmall = base.headlineSmall.copy(fontSize = base.headlineSmall.fontSize.times(scale)),
        titleLarge = base.titleLarge.copy(fontSize = base.titleLarge.fontSize.times(scale)),
        titleMedium = base.titleMedium.copy(fontSize = base.titleMedium.fontSize.times(scale)),
        titleSmall = base.titleSmall.copy(fontSize = base.titleSmall.fontSize.times(scale)),
        bodyLarge = base.bodyLarge.copy(fontSize = base.bodyLarge.fontSize.times(scale)),
        bodyMedium = base.bodyMedium.copy(fontSize = base.bodyMedium.fontSize.times(scale)),
        bodySmall = base.bodySmall.copy(fontSize = base.bodySmall.fontSize.times(scale)),
        labelLarge = base.labelLarge.copy(fontSize = base.labelLarge.fontSize.times(scale)),
        labelMedium = base.labelMedium.copy(fontSize = base.labelMedium.fontSize.times(scale)),
        labelSmall = base.labelSmall.copy(fontSize = base.labelSmall.fontSize.times(scale))
    )
}