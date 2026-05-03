package com.vjn.carromlawscompanion.ui.animation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

enum class QueenCoverBranch { COVER, MISS }

internal const val QC_TOTAL_STEPS = 5
private const val QC_STEP_DURATION_MS = 1200L
private const val QC_TWEEN_MS = 700

@Composable
fun QueenCoverAnimation(
    modifier: Modifier = Modifier,
    autoPlay: Boolean = true,
    skipAnimation: Boolean = !animationsEnabledOnDevice(),
    initialBranch: QueenCoverBranch = QueenCoverBranch.COVER
) {
    var branch by remember { mutableStateOf(initialBranch) }
    var step by remember {
        mutableIntStateOf(if (skipAnimation) QC_TOTAL_STEPS else 0)
    }
    var restartTrigger by remember { mutableIntStateOf(0) }

    LaunchedEffect(autoPlay, skipAnimation, restartTrigger, branch) {
        if (skipAnimation) {
            step = QC_TOTAL_STEPS
            return@LaunchedEffect
        }
        if (!autoPlay) {
            step = 0
            return@LaunchedEffect
        }
        step = 0
        delay(150L)
        for (next in 1..QC_TOTAL_STEPS) {
            step = next
            if (next < QC_TOTAL_STEPS) delay(QC_STEP_DURATION_MS)
        }
    }

    val piecePositions = piecePositionsFor(step, branch)
    val tweenSpec = if (skipAnimation) tween<Float>(0) else tween(QC_TWEEN_MS)

    val strikerX by animateFloatAsState(piecePositions.strikerX, animationSpec = tweenSpec, label = "strikerX")
    val strikerY by animateFloatAsState(piecePositions.strikerY, animationSpec = tweenSpec, label = "strikerY")
    val strikerA by animateFloatAsState(piecePositions.strikerAlpha, animationSpec = tweenSpec, label = "strikerA")
    val queenX by animateFloatAsState(piecePositions.queenX, animationSpec = tweenSpec, label = "queenX")
    val queenY by animateFloatAsState(piecePositions.queenY, animationSpec = tweenSpec, label = "queenY")
    val queenA by animateFloatAsState(piecePositions.queenAlpha, animationSpec = tweenSpec, label = "queenA")
    val whiteX by animateFloatAsState(piecePositions.whiteX, animationSpec = tweenSpec, label = "whiteX")
    val whiteY by animateFloatAsState(piecePositions.whiteY, animationSpec = tweenSpec, label = "whiteY")
    val whiteA by animateFloatAsState(piecePositions.whiteAlpha, animationSpec = tweenSpec, label = "whiteA")

    val pieces = listOf(
        CarromPiece(PieceColor.QUEEN, queenX, queenY, alpha = queenA),
        CarromPiece(PieceColor.WHITE, whiteX, whiteY, alpha = whiteA),
        CarromPiece(PieceColor.STRIKER, strikerX, strikerY, alpha = strikerA, scale = 1.25f)
    )

    val stepLabel = stepLabelFor(step, branch)
    val resultLabel = resultLabelFor(branch)

    Column(modifier = modifier.fillMaxWidth()) {
        CarromBoardCanvas(
            pieces = pieces,
            modifier = Modifier.fillMaxWidth(),
            contentDescription = "Animated diagram of pocketing and covering the Queen. $stepLabel"
        )
        Spacer(Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilterChip(
                selected = branch == QueenCoverBranch.COVER,
                onClick = {
                    if (branch != QueenCoverBranch.COVER) {
                        branch = QueenCoverBranch.COVER
                        restartTrigger++
                    }
                },
                label = { Text("Cover succeeds") }
            )
            FilterChip(
                selected = branch == QueenCoverBranch.MISS,
                onClick = {
                    if (branch != QueenCoverBranch.MISS) {
                        branch = QueenCoverBranch.MISS
                        restartTrigger++
                    }
                },
                label = { Text("Show miss") }
            )
        }
        Spacer(Modifier.height(12.dp))
        Text(
            text = stepLabel,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = if (step >= QC_TOTAL_STEPS) resultLabel else " ",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { (step.coerceAtLeast(1).toFloat() / QC_TOTAL_STEPS) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { restartTrigger++ },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = "Restart",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

internal data class PiecePositions(
    val strikerX: Float, val strikerY: Float, val strikerAlpha: Float,
    val queenX: Float, val queenY: Float, val queenAlpha: Float,
    val whiteX: Float, val whiteY: Float, val whiteAlpha: Float
)

internal fun piecePositionsFor(
    step: Int,
    branch: QueenCoverBranch
): PiecePositions {
    val centre = 0.5f to 0.5f
    val whiteRest = 0.32f to 0.55f
    val strikerBase = 0.5f to 0.842f
    val topRightPocket = 0.92f to 0.08f
    val topLeftPocket = 0.08f to 0.08f
    val missDeflect = 0.78f to 0.30f

    return when (step) {
        0 -> PiecePositions(
            strikerBase.first, strikerBase.second, 1f,
            centre.first, centre.second, 1f,
            whiteRest.first, whiteRest.second, 1f
        )
        1 -> PiecePositions(
            centre.first, centre.second + 0.04f, 1f,
            centre.first, centre.second, 1f,
            whiteRest.first, whiteRest.second, 1f
        )
        2 -> PiecePositions(
            centre.first - 0.05f, centre.second + 0.10f, 1f,
            topRightPocket.first - 0.05f, topRightPocket.second + 0.05f, 1f,
            whiteRest.first, whiteRest.second, 1f
        )
        3 -> PiecePositions(
            strikerBase.first, strikerBase.second, 1f,
            topRightPocket.first, topRightPocket.second, 0f,
            whiteRest.first, whiteRest.second, 1f
        )
        4 -> when (branch) {
            QueenCoverBranch.COVER -> PiecePositions(
                whiteRest.first, whiteRest.second + 0.04f, 1f,
                topRightPocket.first, topRightPocket.second, 0f,
                whiteRest.first - 0.05f, whiteRest.second - 0.06f, 1f
            )
            QueenCoverBranch.MISS -> PiecePositions(
                missDeflect.first, missDeflect.second, 1f,
                topRightPocket.first, topRightPocket.second, 0f,
                whiteRest.first, whiteRest.second, 1f
            )
        }
        else -> when (branch) {
            QueenCoverBranch.COVER -> PiecePositions(
                topLeftPocket.first + 0.10f, topLeftPocket.second + 0.10f, 1f,
                topRightPocket.first, topRightPocket.second, 0f,
                topLeftPocket.first, topLeftPocket.second, 0f
            )
            QueenCoverBranch.MISS -> PiecePositions(
                strikerBase.first, strikerBase.second, 1f,
                centre.first, centre.second, 1f,
                whiteRest.first, whiteRest.second, 1f
            )
        }
    }
}

internal fun stepLabelFor(step: Int, branch: QueenCoverBranch): String = when (step) {
    0 -> "1 of $QC_TOTAL_STEPS: Striker lined up. Queen sits at the centre. One white piece nearby."
    1 -> "2 of $QC_TOTAL_STEPS: Strike — the striker hits the Queen."
    2 -> "3 of $QC_TOTAL_STEPS: Queen travels toward a pocket."
    3 -> "4 of $QC_TOTAL_STEPS: Queen pocketed. You must cover it in this or the very next stroke."
    4 -> when (branch) {
        QueenCoverBranch.COVER ->
            "5 of $QC_TOTAL_STEPS: Cover stroke — striker meets your white piece, sending it toward a pocket."
        QueenCoverBranch.MISS ->
            "5 of $QC_TOTAL_STEPS: Cover stroke misses — no white piece pocketed."
    }
    else -> when (branch) {
        QueenCoverBranch.COVER ->
            "Result: Queen covered. Confirmed for the player."
        QueenCoverBranch.MISS ->
            "Result: Queen returns to the centre (Rule 96)."
    }
}

internal fun resultLabelFor(branch: QueenCoverBranch): String = when (branch) {
    QueenCoverBranch.COVER -> "Queen confirmed — worth 3 points if you win the board"
    QueenCoverBranch.MISS -> "Queen returns to the centre"
}
