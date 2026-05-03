package com.vjn.carromlawscompanion

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

/**
 * Expands carrom abbreviations into readable words for display.
 * - C/m → carromman
 * - C/B → carrom board
 */
fun String.expandAbbreviations(): String {
    return this
        .replace("C/m", "carromman")
        .replace("C/B", "carrom board")
        .replace("c/m", "carromman")
        .replace("c/b", "carrom board")
}

/**
 * Returns an AnnotatedString with all occurrences of [query] highlighted.
 * Case-insensitive. Highlighted text becomes bold and uses the primary color.
 */
@Composable
fun String.highlightSearchTerm(
    query: String,
    highlightColor: Color = MaterialTheme.colorScheme.primary
): AnnotatedString {
    if (query.isBlank() || query.length < 1) {
        return AnnotatedString(this)
    }

    val text = this
    val queryLower = query.lowercase()
    val textLower = text.lowercase()

    return buildAnnotatedString {
        var currentIndex = 0

        while (currentIndex < text.length) {
            val matchIndex = textLower.indexOf(queryLower, currentIndex)

            if (matchIndex == -1) {
                append(text.substring(currentIndex))
                break
            }

            if (matchIndex > currentIndex) {
                append(text.substring(currentIndex, matchIndex))
            }

            withStyle(
                style = SpanStyle(
                    color = highlightColor,
                    fontWeight = FontWeight.Bold
                )
            ) {
                append(text.substring(matchIndex, matchIndex + query.length))
            }

            currentIndex = matchIndex + query.length
        }
    }
}