package com.vjn.carromlawscompanion.ui.animation

import androidx.compose.runtime.Composable

/**
 * Tier-1 visuals for Section I (Standard Equipment). Each rule highlights one feature on the
 * authentic board the canvas already draws.
 */

@Composable
fun PlayingSurfaceVisual() {
    StaticRuleVisual(
        contentDescription = "Highlighted playing surface inside the wooden frame.",
        caption = "73.5–74 cm square, ≥ 8 mm thick plywood. Surface is the area inside the frame.",
        highlights = listOf(BoardHighlight.PlayingSurface)
    )
}

@Composable
fun FramesVisual() {
    StaticRuleVisual(
        contentDescription = "Highlighted wooden frame around the playing surface.",
        caption = "Wooden frame, 1.90–2.54 cm tall, 6.35–7.60 cm wide, with curved inside corners.",
        highlights = listOf(BoardHighlight.Frame)
    )
}

@Composable
fun PocketsVisual() {
    StaticRuleVisual(
        contentDescription = "All four corner pockets highlighted.",
        caption = "Four round corner pockets, 4.45 cm in diameter (±0.15 cm).",
        highlights = listOf(BoardHighlight.Pockets)
    )
}

@Composable
fun BaseLinesVisual() {
    // Highlight the base lines on the bottom side and the two adjacent base circles to make
    // the geometry obvious without cluttering all four sides.
    StaticRuleVisual(
        contentDescription = "Two base lines along the bottom side, with base circles at each end.",
        caption = "On every side: two parallel base lines (47 cm long). Lower line 10.15 cm from frame; upper line 3.18 cm above it. Base circles (3.18 cm) at each end with a 2.54 cm red dot.",
        highlights = listOf(
            BoardHighlight.BaseLineSide(side = 1),
            BoardHighlight.BaseCircle(corner = 2),
            BoardHighlight.BaseCircle(corner = 3)
        )
    )
}

@Composable
fun ArrowsVisual() {
    StaticRuleVisual(
        contentDescription = "Diagonal arrows from the base circles toward each pocket.",
        caption = "Black arrows at 45° in each corner, between the base circles, pointing toward the pocket. 5 cm clearance from the pocket edge.",
        highlights = listOf(BoardHighlight.Arrows)
    )
}

@Composable
fun CentreCircleVisual() {
    StaticRuleVisual(
        contentDescription = "Highlighted red centre circle.",
        caption = "Centre circle (3.18 cm diameter, red) at the exact middle of the board. The Queen rests here at the start of each board.",
        highlights = listOf(BoardHighlight.CentreCircle)
    )
}

@Composable
fun OuterCircleVisual() {
    StaticRuleVisual(
        contentDescription = "Highlighted black outer circle.",
        caption = "Outer circle (17.00 cm diameter ±0.30 cm) drawn around the centre. The full break arrangement must fit inside this circle.",
        highlights = listOf(BoardHighlight.OuterCircle)
    )
}

@Composable
fun CarrommenVisual() {
    // Three sample carrommen lined up side-by-side near the centre.
    val pieces = listOf(
        CarromPiece(PieceColor.WHITE, 0.36f, 0.5f),
        CarromPiece(PieceColor.QUEEN, 0.5f, 0.5f),
        CarromPiece(PieceColor.BLACK, 0.64f, 0.5f)
    )
    StaticRuleVisual(
        contentDescription = "Three carrommen side by side: white, red Queen, black.",
        caption = "Nine whites, nine blacks, one red Queen. Each carromman is 3.02–3.18 cm in diameter and 5.00–5.50 g.",
        pieces = pieces
    )
}

@Composable
fun StrikerVisual() {
    // Striker beside a regular carromman so the size difference is clear.
    val pieces = listOf(
        CarromPiece(PieceColor.WHITE, 0.42f, 0.5f),
        CarromPiece(PieceColor.STRIKER, 0.6f, 0.5f, scale = 1.30f)
    )
    StaticRuleVisual(
        contentDescription = "Striker shown beside a white carromman to compare sizes.",
        caption = "Striker: smooth, round, ≤ 4.13 cm diameter, ≤ 15 g. Visibly larger than a carromman (≈ 3.1 cm).",
        pieces = pieces
    )
}
