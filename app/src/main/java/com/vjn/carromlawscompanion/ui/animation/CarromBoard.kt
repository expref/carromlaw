package com.vjn.carromlawscompanion.ui.animation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics

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
 * Optional emphasis layer drawn over the standard board. Each entry highlights one feature
 * (or a free-form region) so a rule's visual can call attention to specific geometry without
 * redrawing the whole board.
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

@Composable
fun CarromBoardCanvas(
    pieces: List<CarromPiece>,
    modifier: Modifier = Modifier,
    contentDescription: String = "",
    highlights: List<BoardHighlight> = emptyList()
) {
    val dark = isSystemInDarkTheme()
    val boardSurface = if (dark) Color(0xFF6B4A2B) else Color(0xFFE8C99B)
    val frameColor = if (dark) Color(0xFF2A1A10) else Color(0xFF3D2516)
    val lineColor = if (dark) Color(0xFF1A0E07) else Color(0xFF1A0E07)
    val pocketColor = Color(0xFF0E0A07)
    val centreRed = Color(0xFFD32F2F)
    val whitePiece = Color(0xFFFFF8E1)
    val blackPiece = Color(0xFF1C1C1C)
    val queenPiece = Color(0xFFD32F2F)
    val strikerPiece = MaterialTheme.colorScheme.tertiary
    val pieceStroke = Color(0xFF1A0E07).copy(alpha = 0.6f)

    Canvas(
        modifier = modifier
            .aspectRatio(1f)
            .semantics {
                if (contentDescription.isNotEmpty()) {
                    this.contentDescription = contentDescription
                }
            }
    ) {
        val boardSize = size.minDimension
        val frameWidth = boardSize * 0.06f
        val playOrigin = Offset(frameWidth, frameWidth)
        val playSize = boardSize - frameWidth * 2
        val outerCircleRadius = playSize * 0.115f
        val centreCircleRadius = playSize * 0.022f
        val pocketRadius = boardSize * 0.05f
        val pocketInset = frameWidth + pocketRadius * 0.4f
        val pieceBaseRadius = playSize * 0.022f
        val centre = Offset(size.width / 2f, size.height / 2f)
        val lineStrokeWide = boardSize * 0.0045f
        val lineStrokeMid = boardSize * 0.003f
        val lineStrokeThin = boardSize * 0.0022f

        drawRect(color = frameColor, size = size)
        drawRect(
            color = boardSurface,
            topLeft = playOrigin,
            size = Size(playSize, playSize)
        )

        // ---- Base lines (Rule A.4): 8 lines, 2 per side, parallel to each frame side.
        // In playing-surface fractions: lower line at 10.15/74 = 0.137 from frame,
        // upper line at 13.33/74 = 0.180. Lines are 47/74 = 0.635 long, centred.
        val baseLowerOffsetFrac = 0.137f
        val baseUpperOffsetFrac = 0.180f
        val baseLineHalfLengthFrac = 0.318f // half of 47/74

        fun fracToPx(f: Float): Float = playOrigin.x + f * playSize
        fun fracToPy(f: Float): Float = playOrigin.y + f * playSize

        val sideLine = lineStrokeMid
        // Bottom side (from player's perspective)
        listOf(1f - baseLowerOffsetFrac, 1f - baseUpperOffsetFrac).forEach { yFrac ->
            drawLine(
                color = lineColor,
                start = Offset(fracToPx(0.5f - baseLineHalfLengthFrac), fracToPy(yFrac)),
                end = Offset(fracToPx(0.5f + baseLineHalfLengthFrac), fracToPy(yFrac)),
                strokeWidth = sideLine
            )
        }
        // Top side
        listOf(baseLowerOffsetFrac, baseUpperOffsetFrac).forEach { yFrac ->
            drawLine(
                color = lineColor,
                start = Offset(fracToPx(0.5f - baseLineHalfLengthFrac), fracToPy(yFrac)),
                end = Offset(fracToPx(0.5f + baseLineHalfLengthFrac), fracToPy(yFrac)),
                strokeWidth = sideLine
            )
        }
        // Left side
        listOf(baseLowerOffsetFrac, baseUpperOffsetFrac).forEach { xFrac ->
            drawLine(
                color = lineColor,
                start = Offset(fracToPx(xFrac), fracToPy(0.5f - baseLineHalfLengthFrac)),
                end = Offset(fracToPx(xFrac), fracToPy(0.5f + baseLineHalfLengthFrac)),
                strokeWidth = sideLine
            )
        }
        // Right side
        listOf(1f - baseLowerOffsetFrac, 1f - baseUpperOffsetFrac).forEach { xFrac ->
            drawLine(
                color = lineColor,
                start = Offset(fracToPx(xFrac), fracToPy(0.5f - baseLineHalfLengthFrac)),
                end = Offset(fracToPx(xFrac), fracToPy(0.5f + baseLineHalfLengthFrac)),
                strokeWidth = sideLine
            )
        }

        // ---- Base circles (Rule A.4): at the four inner corners of the upper base lines,
        // each with a red 2.54 cm centre dot inside a 3.18 cm outline.
        val baseCircleRadius = playSize * 0.0215f
        val baseCircleRedRadius = playSize * 0.0172f
        val cornerCorners = listOf(
            Offset(fracToPx(baseUpperOffsetFrac), fracToPy(baseUpperOffsetFrac)),
            Offset(fracToPx(1f - baseUpperOffsetFrac), fracToPy(baseUpperOffsetFrac)),
            Offset(fracToPx(baseUpperOffsetFrac), fracToPy(1f - baseUpperOffsetFrac)),
            Offset(fracToPx(1f - baseUpperOffsetFrac), fracToPy(1f - baseUpperOffsetFrac))
        )
        cornerCorners.forEach { c ->
            drawCircle(color = boardSurface, center = c, radius = baseCircleRadius)
            drawCircle(
                color = lineColor,
                center = c,
                radius = baseCircleRadius,
                style = Stroke(width = lineStrokeMid)
            )
            drawCircle(color = centreRed, center = c, radius = baseCircleRedRadius)
        }

        // ---- Corner arrows (Rule A.5): 45° arrows passing between base circles toward
        // each pocket, leaving 5 cm clearance from the pocket edge.
        cornerCorners.forEachIndexed { i, baseCorner ->
            val pocketCorner = listOf(
                Offset(pocketInset, pocketInset),
                Offset(size.width - pocketInset, pocketInset),
                Offset(pocketInset, size.height - pocketInset),
                Offset(size.width - pocketInset, size.height - pocketInset)
            )[i]
            val dx = pocketCorner.x - baseCorner.x
            val dy = pocketCorner.y - baseCorner.y
            val len = kotlin.math.sqrt(dx * dx + dy * dy)
            if (len > 0f) {
                val ux = dx / len
                val uy = dy / len
                val tailMargin = baseCircleRadius * 1.4f
                val headMargin = pocketRadius * 1.25f
                val tail = Offset(baseCorner.x + ux * tailMargin, baseCorner.y + uy * tailMargin)
                val head = Offset(pocketCorner.x - ux * headMargin, pocketCorner.y - uy * headMargin)
                drawLine(
                    color = lineColor,
                    start = tail,
                    end = head,
                    strokeWidth = lineStrokeThin
                )
                val arrowSize = boardSize * 0.018f
                val perpX = -uy
                val perpY = ux
                val barbA = Offset(
                    head.x - ux * arrowSize + perpX * arrowSize * 0.5f,
                    head.y - uy * arrowSize + perpY * arrowSize * 0.5f
                )
                val barbB = Offset(
                    head.x - ux * arrowSize - perpX * arrowSize * 0.5f,
                    head.y - uy * arrowSize - perpY * arrowSize * 0.5f
                )
                val arrowHead = Path().apply {
                    moveTo(head.x, head.y)
                    lineTo(barbA.x, barbA.y)
                    lineTo(barbB.x, barbB.y)
                    close()
                }
                drawPath(arrowHead, color = lineColor)
            }
        }

        // ---- Pockets
        listOf(
            Offset(pocketInset, pocketInset),
            Offset(size.width - pocketInset, pocketInset),
            Offset(pocketInset, size.height - pocketInset),
            Offset(size.width - pocketInset, size.height - pocketInset)
        ).forEach { c ->
            drawCircle(color = pocketColor, radius = pocketRadius, center = c)
            drawCircle(
                color = lineColor.copy(alpha = 0.7f),
                radius = pocketRadius,
                center = c,
                style = Stroke(width = lineStrokeMid)
            )
        }

        // ---- Outer circle (Rule A.7) and centre circle (Rule A.6)
        drawCircle(
            color = lineColor,
            center = centre,
            radius = outerCircleRadius,
            style = Stroke(width = lineStrokeWide)
        )
        drawCircle(color = centreRed, center = centre, radius = centreCircleRadius)
        drawCircle(
            color = lineColor,
            center = centre,
            radius = centreCircleRadius,
            style = Stroke(width = lineStrokeMid)
        )

        // ---- Pieces
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

        // ---- Highlights (drawn last so they sit above pieces and lines)
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
                        // Outline both edges of the wooden frame without overdrawing the surface.
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
                        // Diagonal lines extending arrow direction across the board.
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
        0 -> { // top
            emphasiseLine(xL, fracToPy(lowerOffset), xR, fracToPy(lowerOffset))
            emphasiseLine(xL, fracToPy(upperOffset), xR, fracToPy(upperOffset))
        }
        1 -> { // bottom
            emphasiseLine(xL, fracToPy(1f - lowerOffset), xR, fracToPy(1f - lowerOffset))
            emphasiseLine(xL, fracToPy(1f - upperOffset), xR, fracToPy(1f - upperOffset))
        }
        2 -> { // left
            emphasiseLine(fracToPx(lowerOffset), yL, fracToPx(lowerOffset), yR)
            emphasiseLine(fracToPx(upperOffset), yL, fracToPx(upperOffset), yR)
        }
        3 -> { // right
            emphasiseLine(fracToPx(1f - lowerOffset), yL, fracToPx(1f - lowerOffset), yR)
            emphasiseLine(fracToPx(1f - upperOffset), yL, fracToPx(1f - upperOffset), yR)
        }
    }
}
