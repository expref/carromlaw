package com.vjn.carromlawscompanion.ui.animation

import androidx.compose.runtime.Composable

/**
 * Tier-2 visuals for stroke definitions and break/slip mechanics:
 * 25 Stroke, 33 Strike Not Push, 44 Valid Break, 127a/b Striker Slips.
 */

@Composable
fun StrokeVisual() {
    // Striker on the player's base lines, target white piece in the centre, suggesting a direct hit.
    val pieces = listOf(
        CarromPiece(PieceColor.STRIKER, 0.5f, 0.842f, scale = 1.25f),
        CarromPiece(PieceColor.WHITE, 0.5f, 0.5f),
        CarromPiece(PieceColor.QUEEN, 0.42f, 0.5f, alpha = 0.6f)
    )
    StaticRuleVisual(
        contentDescription = "Striker on the bottom base lines, aimed up the board at a white carromman in the centre.",
        caption = "Stroke: hitting a C/m with the striker — directly (straight line) or indirectly (off the frame).",
        pieces = pieces
    )
}

@Composable
fun StrikeNotPushVisual() {
    // Show striker on the base lines with a "strike zone" highlight rather than animated motion.
    val pieces = listOf(
        CarromPiece(PieceColor.STRIKER, 0.5f, 0.842f, scale = 1.25f)
    )
    StaticRuleVisual(
        contentDescription = "Striker resting on the base lines; a strike must be a finger flick, not an elbow push.",
        caption = "Strike, don't push. The striker is FLICKED with a fingertip — never propelled by jerking the elbow or arm.",
        pieces = pieces,
        highlights = listOf(BoardHighlight.BaseLineSide(side = 1))
    )
}

@Composable
fun ValidBreakVisual() {
    // Striker just touching the nearest C/m of the standard 41a arrangement.
    val pieces = piecesFor(BREAK_TOTAL_STEPS).toMutableList().also {
        // Add a striker just touching the nearest outer-ring black coin from the bottom side.
        // The break arrangement's nearest piece on the bottom faces (angle 90°) is at (0.5, 0.5+rOuterAligned).
        // pieceR = 0.022, so place striker centre slightly below, just kissing the C/m.
        val targetY = 0.5f + 4f * 0.022f          // outer aligned bottom y ≈ 0.588
        val strikerY = targetY + 0.022f + 0.0275f // piece radius + striker radius
        it += CarromPiece(PieceColor.STRIKER, 0.5f, strikerY, scale = 1.25f)
    }
    StaticRuleVisual(
        contentDescription = "Striker barely touching the nearest carromman of the break arrangement.",
        caption = "A break is valid as soon as the striker touches ANY C/m, even slightly.",
        pieces = pieces
    )
}

@Composable
fun StrikerSlipsVisual() {
    // Striker has slipped FORWARD off the base lines — past the upper base line, into play.
    // Bottom base lines are at yFrac ≈ 1 - 0.180 = 0.820 (upper) and 0.863 (lower).
    val pieces = listOf(
        CarromPiece(PieceColor.STRIKER, 0.5f, 0.78f, scale = 1.25f)
    )
    StaticRuleVisual(
        contentDescription = "Striker slipped past the upper base line into the playing area.",
        caption = "Striker slips: if it leaves a base line or base circle (even without touching a C/m), the stroke counts as taken.",
        pieces = pieces,
        highlights = listOf(BoardHighlight.BaseLineSide(side = 1))
    )
}

@Composable
fun SlipNoMovementVisual() {
    // Striker still touching both base lines after a slip — no stroke counted.
    val pieces = listOf(
        CarromPiece(PieceColor.STRIKER, 0.46f, 0.842f, scale = 1.25f)
    )
    StaticRuleVisual(
        contentDescription = "Striker shifted slightly along the base lines but still touching both — no stroke registered.",
        caption = "If the striker shifts but stays on its base line/circle and doesn't move any C/m, no stroke is taken.",
        pieces = pieces,
        highlights = listOf(BoardHighlight.BaseLineSide(side = 1))
    )
}
