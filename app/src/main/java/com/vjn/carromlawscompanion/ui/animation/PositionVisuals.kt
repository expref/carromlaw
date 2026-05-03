package com.vjn.carromlawscompanion.ui.animation

import androidx.compose.runtime.Composable

/**
 * Tier-1 visuals for sitting position (Section III) and base-line stroke geometry (Section XVI).
 * Player markers are rendered as STRIKER-coloured discs placed outside the playing surface
 * fractions [0,1] — they sit near the frame edges to indicate seating.
 */

@Composable
fun SinglesSittingVisual() {
    // Two markers opposite each other (top and bottom), outside the play area.
    val pieces = listOf(
        CarromPiece(PieceColor.STRIKER, 0.5f, -0.06f, scale = 1.6f),
        CarromPiece(PieceColor.STRIKER, 0.5f, 1.06f, scale = 1.6f)
    )
    StaticRuleVisual(
        contentDescription = "Two players seated opposite each other at the top and bottom of the board.",
        caption = "Singles: the two players sit opposite each other on facing sides of the board.",
        pieces = pieces
    )
}

@Composable
fun DoublesSittingVisual() {
    // Four markers around the four sides.
    val pieces = listOf(
        CarromPiece(PieceColor.STRIKER, 0.5f, -0.06f, scale = 1.6f),
        CarromPiece(PieceColor.STRIKER, 0.5f, 1.06f, scale = 1.6f),
        CarromPiece(PieceColor.STRIKER, -0.06f, 0.5f, scale = 1.6f),
        CarromPiece(PieceColor.STRIKER, 1.06f, 0.5f, scale = 1.6f)
    )
    StaticRuleVisual(
        contentDescription = "Four players seated around all four sides of the board.",
        caption = "Doubles: partners sit opposite each other; all four sides occupied.",
        pieces = pieces
    )
}

@Composable
fun ImaginaryLinesVisual() {
    StaticRuleVisual(
        contentDescription = "Diagonal imaginary lines extending the corner arrows across the board.",
        caption = "Imaginary lines extend the corner arrows. They mark the boundary your body and elbow may not cross during a stroke.",
        highlights = listOf(BoardHighlight.ImaginaryLines)
    )
}

@Composable
fun BodyBeyondLinesVisual() {
    // Striker placed on the player's base lines (bottom side) and the imaginary lines highlighted
    // so the rule's "no body part beyond these lines" constraint is visible.
    val pieces = listOf(
        CarromPiece(PieceColor.STRIKER, 0.5f, 0.86f, scale = 1.25f)
    )
    StaticRuleVisual(
        contentDescription = "Striker placed near the player's base lines with the imaginary diagonal boundary highlighted.",
        caption = "No part of the body (except the playing hand) may cross these imaginary lines extending the arrows.",
        highlights = listOf(BoardHighlight.ImaginaryLines),
        pieces = pieces
    )
}

@Composable
fun StrikerOnBaseLinesVisual() {
    // Striker straddling both base lines on the bottom side.
    val pieces = listOf(
        CarromPiece(PieceColor.STRIKER, 0.5f, 0.842f, scale = 1.25f)
    )
    StaticRuleVisual(
        contentDescription = "Striker placed touching both base lines on the bottom side.",
        caption = "While taking the stroke, the striker must touch BOTH base lines on your side.",
        highlights = listOf(BoardHighlight.BaseLineSide(side = 1)),
        pieces = pieces
    )
}

@Composable
fun BaseCircleStrokeVisual() {
    // Striker placed inside the lower-right base circle (corner=3 = bottom-right) so it covers
    // the circle and stays clear of the arrow.
    val pieces = listOf(
        CarromPiece(PieceColor.STRIKER, 0.180f, 0.820f, scale = 1.25f)
    )
    StaticRuleVisual(
        contentDescription = "Striker covering a base circle without touching the corner arrow.",
        caption = "Stroke from a base circle: the striker must fully cover the base circle but must NOT touch the arrow.",
        highlights = listOf(BoardHighlight.BaseCircle(corner = 2)),
        pieces = pieces
    )
}
