package com.vjn.carromlawscompanion.ui.animation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Common scaffold for non-animated rule visuals: a single-frame board diagram with a caption.
 * Animated rules (41a, 92, …) build their own Column with step controls instead.
 */
@Composable
fun StaticRuleVisual(
    contentDescription: String,
    caption: String,
    pieces: List<CarromPiece> = emptyList(),
    highlights: List<BoardHighlight> = emptyList(),
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        CarromBoardCanvas(
            pieces = pieces,
            modifier = Modifier.fillMaxWidth(),
            contentDescription = contentDescription,
            highlights = highlights
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = caption,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
    }
}
