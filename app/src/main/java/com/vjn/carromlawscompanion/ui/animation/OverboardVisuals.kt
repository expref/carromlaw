package com.vjn.carromlawscompanion.ui.animation

import androidx.compose.runtime.Composable

/**
 * Tier-2 visuals for jumped/overboard placements (Section XII) and striker jump (116).
 * Each shows the FINAL placement state the Umpire produces, since that's the rule's content.
 */

@Composable
fun JumpedPlacementVisual() {
    // Single C/m placed at the centre circle (where the Queen normally rests).
    val pieces = listOf(
        CarromPiece(PieceColor.WHITE, 0.5f, 0.5f)
    )
    StaticRuleVisual(
        contentDescription = "A jumped carromman placed back on the centre circle by the Umpire.",
        caption = "If a C/m or Queen jumps off the board, the Umpire places it on the Centre Circle (covering it as fully as possible).",
        pieces = pieces,
        highlights = listOf(BoardHighlight.CentreCircle)
    )
}

@Composable
fun QueenAndCmJumpVisual() {
    // Queen at centre; jumped C/m touching it on the side opposite the player having the turn.
    // Player at bottom => "opposite direction" is upward (toward top of board).
    val pieces = listOf(
        CarromPiece(PieceColor.QUEEN, 0.5f, 0.5f),
        CarromPiece(PieceColor.WHITE, 0.5f, 0.5f - 2f * 0.022f)
    )
    StaticRuleVisual(
        contentDescription = "Queen on centre with a jumped white carromman touching it on the side opposite the player.",
        caption = "Queen + a C/m jump together: Queen placed first on centre, then the C/m touching the Queen on the side OPPOSITE the player whose turn it is.",
        pieces = pieces,
        highlights = listOf(BoardHighlight.CentreCircle)
    )
}

@Composable
fun BothColorsJumpVisual() {
    // Player at bottom plays — say white. Their C/m placed first on centre; opponent's (black)
    // touches it on the side opposite the player (upward).
    val pieces = listOf(
        CarromPiece(PieceColor.WHITE, 0.5f, 0.5f),
        CarromPiece(PieceColor.BLACK, 0.5f, 0.5f - 2f * 0.022f)
    )
    StaticRuleVisual(
        contentDescription = "The player's white carromman on centre with the opponent's black carromman touching it on the far side.",
        caption = "Both colours jump: the player's own C/m goes on centre first; the opponent's C/m touches it on the side opposite the player.",
        pieces = pieces,
        highlights = listOf(BoardHighlight.CentreCircle)
    )
}

@Composable
fun StrikerJumpVisual() {
    // Striker shown well outside the play area to suggest having jumped over the board, with a
    // hint coin near the centre to imply it had pocketed before leaving.
    val pieces = listOf(
        CarromPiece(PieceColor.STRIKER, 1.10f, -0.06f, scale = 1.25f),
        CarromPiece(PieceColor.WHITE, 0.5f, 0.5f, alpha = 0.55f)
    )
    StaticRuleVisual(
        contentDescription = "Striker shown beyond the top-right corner of the board to indicate a jump.",
        caption = "If the striker jumps clear of the board during a stroke, the turn still continues — provided the stroke also pocketed your own C/m or the Queen.",
        pieces = pieces
    )
}
