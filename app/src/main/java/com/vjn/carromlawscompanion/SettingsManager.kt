package com.vjn.carromlawscompanion

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * Manages user preferences like font size.
 */
class SettingsManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(
        "carrom_settings",
        Context.MODE_PRIVATE
    )

    private val fontSizeKey = "font_size"

    fun getFontSize(): FontSize {
        val name = prefs.getString(fontSizeKey, FontSize.MEDIUM.name) ?: FontSize.MEDIUM.name
        return try {
            FontSize.valueOf(name)
        } catch (e: Exception) {
            FontSize.MEDIUM
        }
    }

    fun setFontSize(size: FontSize) {
        prefs.edit().putString(fontSizeKey, size.name).apply()
    }
}

enum class FontSize(val displayName: String, val scale: Float) {
    SMALL("Small", 0.85f),
    MEDIUM("Medium", 1.0f),
    LARGE("Large", 1.2f),
    EXTRA_LARGE("Extra Large", 1.4f)
}

/**
 * CompositionLocal that provides the current font scale to all screens.
 * Default is 1.0f (no scaling).
 */
val LocalFontScale = staticCompositionLocalOf { 1.0f }