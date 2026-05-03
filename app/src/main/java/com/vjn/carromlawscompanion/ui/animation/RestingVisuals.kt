package com.vjn.carromlawscompanion.ui.animation

import androidx.compose.runtime.Composable

/**
 * Tier-2 visuals for unusual resting positions (Section XIII) and Queen falling in (113).
 * Top-down 2D can't fully convey 3D effects (a C/m on its rim, a striker stacked on a C/m),
 * so each visual pairs the closest 2D approximation with a clear caption.
 */

@Composable
fun StandingOnRimVisual() {
    // 2D can't really show vertical orientation. Use a slightly smaller, brightly highlighted
    // disc to suggest a special state, with a circular emphasis ring around it.
    val pieces = listOf(
        CarromPiece(PieceColor.WHITE, 0.55f, 0.5f, scale = 0.55f)
    )
    StaticRuleVisual(
        contentDescription = "A carromman shown small to indicate it is standing on its rim.",
        caption = "If a C/m or Queen comes to rest standing on its rim (vertical), it is allowed to remain as it is.",
        pieces = pieces,
        highlights = listOf(BoardHighlight.Circle(0.55f, 0.5f, 0.025f))
    )
}

@Composable
fun OverlappingVisual() {
    // Two C/m partially stacked — render with one slightly transparent + offset, ringed.
    val pieces = listOf(
        CarromPiece(PieceColor.BLACK, 0.5f, 0.5f),
        CarromPiece(PieceColor.WHITE, 0.515f, 0.49f, alpha = 0.85f, scale = 0.92f)
    )
    StaticRuleVisual(
        contentDescription = "Two carrommen partially overlapping each other near the centre.",
        caption = "If two C/m (or a C/m and the Queen) overlap each other, leave them undisturbed.",
        pieces = pieces,
        highlights = listOf(BoardHighlight.Circle(0.508f, 0.495f, 0.04f))
    )
}

@Composable
fun StrikerOnCmVisual() {
    // Striker resting on top of a C/m — render the striker on top with elevated alpha.
    val pieces = listOf(
        CarromPiece(PieceColor.WHITE, 0.5f, 0.5f),
        CarromPiece(PieceColor.STRIKER, 0.5f, 0.5f, alpha = 0.65f, scale = 1.30f)
    )
    StaticRuleVisual(
        contentDescription = "Striker stacked on top of a white carromman near the centre.",
        caption = "Striker resting on a C/m: the Umpire lifts the striker without disturbing the C/m. If disturbed, the original position is restored.",
        pieces = pieces
    )
}

@Composable
fun PocketMouthFallVisual() {
    // C/m perched at the mouth of the top-right pocket; striker just behind it.
    val pieces = listOf(
        CarromPiece(PieceColor.WHITE, 0.890f, 0.110f),
        CarromPiece(PieceColor.STRIKER, 0.860f, 0.140f, alpha = 0.80f, scale = 1.25f)
    )
    StaticRuleVisual(
        contentDescription = "White carromman perched at the mouth of the top-right pocket with a striker beside it.",
        caption = "If a C/m at the pocket mouth loses its centre of gravity while the Umpire removes the striker and falls in, it is deemed pocketed.",
        pieces = pieces,
        highlights = listOf(BoardHighlight.Pocket(corner = 1))
    )
}

@Composable
fun CmOnStrikerVisual() {
    // Reverse stack — C/m on the striker.
    val pieces = listOf(
        CarromPiece(PieceColor.STRIKER, 0.5f, 0.5f, scale = 1.30f),
        CarromPiece(PieceColor.WHITE, 0.5f, 0.5f, scale = 0.90f)
    )
    StaticRuleVisual(
        contentDescription = "White carromman stacked on top of the striker near the centre.",
        caption = "C/m resting on the striker: Umpire lifts the C/m and replaces it as close as possible to the position it would occupy if the striker were not there.",
        pieces = pieces
    )
}

@Composable
fun StrikerFallsInVisual() {
    // Striker about to fall into top-right pocket while a C/m is being removed.
    val pieces = listOf(
        CarromPiece(PieceColor.STRIKER, 0.890f, 0.110f, scale = 1.25f),
        CarromPiece(PieceColor.WHITE, 0.860f, 0.140f, alpha = 0.55f)
    )
    StaticRuleVisual(
        contentDescription = "Striker perched at the mouth of the top-right pocket, about to fall in.",
        caption = "If the striker at the pocket mouth loses its centre of gravity while the C/m is removed and falls in, the striker is deemed pocketed (and a Due is declared).",
        pieces = pieces,
        highlights = listOf(BoardHighlight.Pocket(corner = 1))
    )
}

@Composable
fun PrecariousFallVisual() {
    val pieces = listOf(
        CarromPiece(PieceColor.WHITE, 0.890f, 0.110f)
    )
    StaticRuleVisual(
        contentDescription = "White carromman teetering on the edge of the top-right pocket.",
        caption = "A C/m resting precariously at the mouth of a pocket that falls in for any reason is considered properly pocketed.",
        pieces = pieces,
        highlights = listOf(BoardHighlight.Pocket(corner = 1))
    )
}

@Composable
fun QueenFallsInVisual() {
    val pieces = listOf(
        CarromPiece(PieceColor.QUEEN, 0.890f, 0.110f)
    )
    StaticRuleVisual(
        contentDescription = "Queen teetering on the edge of the top-right pocket.",
        caption = "Same as Rule 71 — if the Queen rests precariously at a pocket mouth and falls in for any reason, it is considered duly pocketed.",
        pieces = pieces,
        highlights = listOf(BoardHighlight.Pocket(corner = 1))
    )
}
