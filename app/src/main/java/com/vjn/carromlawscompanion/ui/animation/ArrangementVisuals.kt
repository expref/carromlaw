package com.vjn.carromlawscompanion.ui.animation

import androidx.compose.runtime.Composable

/**
 * Tier-2 visuals for shot terminology in Section II: Pair (16a) and Cannon (16b).
 * Both show a target C/m near a corner pocket and a placed C/m lined up with it; the only
 * difference is whether there is a gap or the two carrommen touch.
 */

@Composable
fun PairVisual() {
    val pieces = listOf(
        // Target C/m closer to the bottom-right pocket
        CarromPiece(PieceColor.WHITE, 0.78f, 0.74f),
        // Placed C/m a coin-and-a-half away, on the line from queen toward the target
        CarromPiece(PieceColor.WHITE, 0.66f, 0.60f)
    )
    StaticRuleVisual(
        contentDescription = "Two white carrommen lined up toward the bottom-right pocket with a small gap between them.",
        caption = "Pair: a C/m placed near another within the Outer Circle, with a gap between them, lined up toward a pocket.",
        pieces = pieces,
        highlights = listOf(BoardHighlight.Pocket(corner = 3))
    )
}

@Composable
fun CannonVisual() {
    val pieces = listOf(
        CarromPiece(PieceColor.WHITE, 0.78f, 0.74f),
        // Touching the target C/m (centres separated by one piece diameter ≈ 0.044)
        CarromPiece(PieceColor.WHITE, 0.747f, 0.706f)
    )
    StaticRuleVisual(
        contentDescription = "Two white carrommen touching, lined up toward the bottom-right pocket.",
        caption = "Cannon: like a Pair, but the placed C/m TOUCHES the existing one — no gap.",
        pieces = pieces,
        highlights = listOf(BoardHighlight.Pocket(corner = 3))
    )
}
