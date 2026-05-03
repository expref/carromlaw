package com.vjn.carromlawscompanion.ui.animation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.vjn.carromlawscompanion.R

enum class PieceColor { WHITE, BLACK, QUEEN, STRIKER }

data class CarromPiece(
    val color: PieceColor,
    val xFraction: Float,
    val yFraction: Float,
    val alpha: Float = 1f,
    val scale: Float = 1f,
    val pocketed: Boolean = false
)

/**
 * Optional emphasis layer drawn over the photographed board. Each entry highlights one feature
 * (or a free-form region) so a rule's visual can call attention to specific geometry.
 *
 * Coordinates are computed from board fractions tuned to match `R.drawable.carrom_board`.
 */
sealed class BoardHighlight {
    object PlayingSurface : BoardHighlight()
    object Frame : BoardHighlight()
    object Pockets : BoardHighlight()
    object BaseLines : BoardHighlight()
    object BaseCircles : BoardHighlight()
    object Arrows : BoardHighlight()
    object CentreCircle : BoardHighlight()
    object OuterCircle : BoardHighlight()
    object ImaginaryLines : BoardHighlight()

    /** Highlight a single corner pocket (0=TL, 1=TR, 2=BL, 3=BR). */
    data class Pocket(val corner: Int) : BoardHighlight()

    /** Highlight one corner's base circle (0=TL, 1=TR, 2=BL, 3=BR). */
    data class BaseCircle(val corner: Int) : BoardHighlight()

    /** Highlight one side's pair of base lines (0=top, 1=bottom, 2=left, 3=right). */
    data class BaseLineSide(val side: Int) : BoardHighlight()

    /** Free-form circular emphasis at fractional coordinates of the playing surface. */
    data class Circle(val cxFrac: Float, val cyFrac: Float, val radiusFrac: Float) : BoardHighlight()

    /** Free-form rectangular emphasis at fractional coordinates of the playing surface. */
    data class Rect(val xFrac: Float, val yFrac: Float, val wFrac: Float, val hFrac: Float) : BoardHighlight()
}

// Constants tuned to the photographed board (R.drawable.carrom_board). The dark wooden frame
// takes up roughly 8.5% of the image on each side; pockets sit about 12% in from each edge.
private const val BOARD_FRAME_FRAC = 0.085f
private const val BOARD_POCKET_RADIUS_FRAC = 0.040f
private const val BOARD_POCKET_INSET_BUMP = 0.034f // distance from inner frame edge to pocket centre

@Composable
fun CarromBoardCanvas(
    pieces: List<CarromPiece>,
    modifier: Modifier = Modifier,
    contentDescription: String = "",
    highlights: List<BoardHighlight> = emptyList()
) {
    val whitePiece = Color(0xFFFFF8E1)
    val blackPiece = Color(0xFF1C1C1C)
    val queenPiece = Color(0xFFD32F2F)
    val strikerPiece = Color(0xFF00897B) // teal — distinct from white/black pieces, queen red, and highlight amber
    val pieceStroke = Color(0xFF1A0E07).copy(alpha = 0.6f)
    val frameMaskColor = Color(0xFF2D1A0E) // dark espresso to match the photographed frame

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .semantics {
                if (contentDescription.isNotEmpty()) {
                    this.contentDescription = contentDescription
                }
            }
    ) {
        Image(
            painter = painterResource(R.drawable.carrom_board),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
        Canvas(modifier = Modifier.fillMaxSize()) {
            val boardSize = size.minDimension
            val frameWidth = boardSize * BOARD_FRAME_FRAC
            val playOrigin = Offset(frameWidth, frameWidth)
            val playSize = boardSize - frameWidth * 2
            val outerCircleRadius = playSize * 0.115f
            val centreCircleRadius = playSize * 0.022f
            val pocketRadius = boardSize * BOARD_POCKET_RADIUS_FRAC
            val pocketInset = frameWidth + boardSize * BOARD_POCKET_INSET_BUMP
            val pieceBaseRadius = playSize * 0.022f
            val centre = Offset(size.width / 2f, size.height / 2f)
            val lineStrokeThin = boardSize * 0.0022f

            val baseLowerOffsetFrac = 0.137f
            val baseUpperOffsetFrac = 0.180f
            val baseLineHalfLengthFrac = 0.318f

            fun fracToPx(f: Float): Float = playOrigin.x + f * playSize
            fun fracToPy(f: Float): Float = playOrigin.y + f * playSize

            // ---- Mask branding on the wooden frame.
            // Top: "Geologic" + logo strip, ~middle 40% of the top frame.
            // Bottom: "Carrom 720" strip, ~middle 26% of the bottom frame.
            drawRect(
                color = frameMaskColor,
                topLeft = Offset(size.width * 0.30f, size.height * 0.005f),
                size = Size(size.width * 0.40f, size.height * 0.070f)
            )
            drawRect(
                color = frameMaskColor,
                topLeft = Offset(size.width * 0.37f, size.height * 0.925f),
                size = Size(size.width * 0.26f, size.height * 0.070f)
            )

            val baseCircleRadius = playSize * 0.0215f
            val cornerCorners = listOf(
                Offset(fracToPx(baseUpperOffsetFrac), fracToPy(baseUpperOffsetFrac)),
                Offset(fracToPx(1f - baseUpperOffsetFrac), fracToPy(baseUpperOffsetFrac)),
                Offset(fracToPx(baseUpperOffsetFrac), fracToPy(1f - baseUpperOffsetFrac)),
                Offset(fracToPx(1f - baseUpperOffsetFrac), fracToPy(1f - baseUpperOffsetFrac))
            )

            // ---- Pieces (drawn over the photographed board)
            pieces.filterNot { it.pocketed }.forEach { piece ->
                val px = playOrigin.x + piece.xFraction * playSize
                val py = playOrigin.y + piece.yFraction * playSize
                val r = pieceBaseRadius * piece.scale
                val fill = when (piece.color) {
                    PieceColor.WHITE -> whitePiece
                    PieceColor.BLACK -> blackPiece
                    PieceColor.QUEEN -> queenPiece
                    PieceColor.STRIKER -> strikerPiece
                }
                drawCircle(
                    color = fill.copy(alpha = piece.alpha),
                    center = Offset(px, py),
                    radius = r
                )
                drawCircle(
                    color = pieceStroke.copy(alpha = piece.alpha * 0.8f),
                    center = Offset(px, py),
                    radius = r,
                    style = Stroke(width = lineStrokeThin)
                )
            }

            // ---- Highlights (drawn last so they sit above pieces)
            if (highlights.isNotEmpty()) {
                val accent = Color(0xFFFFC107)
                val accentSoft = accent.copy(alpha = 0.35f)
                val emphasisStroke = boardSize * 0.008f
                val emphasisHalo = boardSize * 0.014f

                fun emphasiseCircle(cx: Float, cy: Float, r: Float) {
                    drawCircle(color = accentSoft, center = Offset(cx, cy), radius = r + emphasisHalo)
                    drawCircle(color = accent, center = Offset(cx, cy), radius = r + emphasisStroke / 2f, style = Stroke(width = emphasisStroke))
                }

                fun emphasiseRect(x: Float, y: Float, w: Float, h: Float) {
                    drawRect(color = accentSoft, topLeft = Offset(x - emphasisHalo, y - emphasisHalo), size = Size(w + emphasisHalo * 2, h + emphasisHalo * 2))
                    drawRect(color = accent, topLeft = Offset(x, y), size = Size(w, h), style = Stroke(width = emphasisStroke))
                }

                fun emphasiseLine(x1: Float, y1: Float, x2: Float, y2: Float) {
                    drawLine(color = accentSoft, start = Offset(x1, y1), end = Offset(x2, y2), strokeWidth = emphasisStroke * 2.5f)
                    drawLine(color = accent, start = Offset(x1, y1), end = Offset(x2, y2), strokeWidth = emphasisStroke)
                }

                val pocketCenters = listOf(
                    Offset(pocketInset, pocketInset),
                    Offset(size.width - pocketInset, pocketInset),
                    Offset(pocketInset, size.height - pocketInset),
                    Offset(size.width - pocketInset, size.height - pocketInset)
                )

                highlights.forEach { h ->
                    when (h) {
                        BoardHighlight.PlayingSurface ->
                            emphasiseRect(playOrigin.x, playOrigin.y, playSize, playSize)
                        BoardHighlight.Frame -> {
                            drawRect(color = accent, size = size, style = Stroke(width = emphasisStroke))
                            drawRect(color = accent, topLeft = playOrigin, size = Size(playSize, playSize), style = Stroke(width = emphasisStroke))
                        }
                        BoardHighlight.Pockets ->
                            pocketCenters.forEach { c -> emphasiseCircle(c.x, c.y, pocketRadius) }
                        is BoardHighlight.Pocket ->
                            pocketCenters.getOrNull(h.corner)?.let { emphasiseCircle(it.x, it.y, pocketRadius) }
                        BoardHighlight.BaseLines -> {
                            val sides = listOf(0, 1, 2, 3)
                            sides.forEach { drawSideBaseLineEmphasis(it, baseLowerOffsetFrac, baseUpperOffsetFrac, baseLineHalfLengthFrac, ::fracToPx, ::fracToPy, ::emphasiseLine) }
                        }
                        is BoardHighlight.BaseLineSide ->
                            drawSideBaseLineEmphasis(h.side, baseLowerOffsetFrac, baseUpperOffsetFrac, baseLineHalfLengthFrac, ::fracToPx, ::fracToPy, ::emphasiseLine)
                        BoardHighlight.BaseCircles ->
                            cornerCorners.forEach { c -> emphasiseCircle(c.x, c.y, baseCircleRadius) }
                        is BoardHighlight.BaseCircle ->
                            cornerCorners.getOrNull(h.corner)?.let { emphasiseCircle(it.x, it.y, baseCircleRadius) }
                        BoardHighlight.Arrows -> {
                            cornerCorners.forEachIndexed { i, baseCorner ->
                                val pocketCorner = pocketCenters[i]
                                val dx = pocketCorner.x - baseCorner.x
                                val dy = pocketCorner.y - baseCorner.y
                                val len = kotlin.math.sqrt(dx * dx + dy * dy)
                                if (len > 0f) {
                                    val ux = dx / len; val uy = dy / len
                                    val tail = Offset(baseCorner.x + ux * baseCircleRadius * 1.4f, baseCorner.y + uy * baseCircleRadius * 1.4f)
                                    val head = Offset(pocketCorner.x - ux * pocketRadius * 1.25f, pocketCorner.y - uy * pocketRadius * 1.25f)
                                    emphasiseLine(tail.x, tail.y, head.x, head.y)
                                }
                            }
                        }
                        BoardHighlight.CentreCircle -> emphasiseCircle(centre.x, centre.y, centreCircleRadius)
                        BoardHighlight.OuterCircle -> emphasiseCircle(centre.x, centre.y, outerCircleRadius)
                        BoardHighlight.ImaginaryLines -> {
                            cornerCorners.forEachIndexed { i, baseCorner ->
                                val opposite = cornerCorners[3 - i]
                                emphasiseLine(baseCorner.x, baseCorner.y, opposite.x, opposite.y)
                            }
                        }
                        is BoardHighlight.Circle ->
                            emphasiseCircle(fracToPx(h.cxFrac), fracToPy(h.cyFrac), h.radiusFrac * playSize)
                        is BoardHighlight.Rect ->
                            emphasiseRect(fracToPx(h.xFrac), fracToPy(h.yFrac), h.wFrac * playSize, h.hFrac * playSize)
                    }
                }
            }
        }
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawSideBaseLineEmphasis(
    side: Int,
    lowerOffset: Float,
    upperOffset: Float,
    halfLength: Float,
    fracToPx: (Float) -> Float,
    fracToPy: (Float) -> Float,
    emphasiseLine: (Float, Float, Float, Float) -> Unit
) {
    val xL = fracToPx(0.5f - halfLength)
    val xR = fracToPx(0.5f + halfLength)
    val yL = fracToPy(0.5f - halfLength)
    val yR = fracToPy(0.5f + halfLength)
    when (side) {
        0 -> {
            emphasiseLine(xL, fracToPy(lowerOffset), xR, fracToPy(lowerOffset))
            emphasiseLine(xL, fracToPy(upperOffset), xR, fracToPy(upperOffset))
        }
        1 -> {
            emphasiseLine(xL, fracToPy(1f - lowerOffset), xR, fracToPy(1f - lowerOffset))
            emphasiseLine(xL, fracToPy(1f - upperOffset), xR, fracToPy(1f - upperOffset))
        }
        2 -> {
            emphasiseLine(fracToPx(lowerOffset), yL, fracToPx(lowerOffset), yR)
            emphasiseLine(fracToPx(upperOffset), yL, fracToPx(upperOffset), yR)
        }
        3 -> {
            emphasiseLine(fracToPx(1f - lowerOffset), yL, fracToPx(1f - lowerOffset), yR)
            emphasiseLine(fracToPx(1f - upperOffset), yL, fracToPx(1f - upperOffset), yR)
        }
    }
}
