package com.vjn.carromlawscompanion.ui.animation

import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin

internal const val BREAK_TOTAL_STEPS = 5
private const val STEP_DURATION_MS = 1300L

@Composable
fun BreakArrangementAnimation(
    modifier: Modifier = Modifier,
    autoPlay: Boolean = true,
    skipAnimation: Boolean = !animationsEnabledOnDevice()
) {
    var step by remember {
        mutableIntStateOf(
            when {
                skipAnimation -> BREAK_TOTAL_STEPS
                !autoPlay -> 1
                else -> 0
            }
        )
    }
    var restartTrigger by remember { mutableIntStateOf(0) }

    LaunchedEffect(autoPlay, skipAnimation, restartTrigger) {
        if (skipAnimation) {
            step = BREAK_TOTAL_STEPS
            return@LaunchedEffect
        }
        if (!autoPlay) {
            step = 1
            return@LaunchedEffect
        }
        step = 0
        delay(150L)
        for (next in 1..BREAK_TOTAL_STEPS) {
            step = next
            if (next < BREAK_TOTAL_STEPS) delay(STEP_DURATION_MS)
        }
    }

    val pieces = remember(step) { piecesFor(step) }
    val label = labelFor(step)

    Column(modifier = modifier.fillMaxWidth()) {
        CarromBoardCanvas(
            pieces = pieces,
            modifier = Modifier.fillMaxWidth(),
            contentDescription = "Animated diagram of the break arrangement. $label"
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
        Spacer(Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { (step.coerceAtLeast(1).toFloat() / BREAK_TOTAL_STEPS) },
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

internal fun labelFor(step: Int): String = when (step) {
    0 -> "Step 1 of $BREAK_TOTAL_STEPS: Empty board, ready to arrange"
    1 -> "Step 1 of $BREAK_TOTAL_STEPS: Place the Queen at centre"
    2 -> "Step 2 of $BREAK_TOTAL_STEPS: First ring — alternating black and white around the Queen"
    3 -> "Step 3 of $BREAK_TOTAL_STEPS: Three whites form a Y-pattern with the inner whites"
    4 -> "Step 4 of $BREAK_TOTAL_STEPS: Remaining pieces fill alternately"
    else -> "All pieces in place — compact, within the Outer Circle"
}

internal fun piecesFor(step: Int): List<CarromPiece> {
    if (step <= 0) return emptyList()
    val cx = 0.5f
    val cy = 0.5f
    val pieceR = 0.022f
    val r1 = 2f * pieceR
    val r2 = 4f * pieceR
    val out = mutableListOf<CarromPiece>()

    out += CarromPiece(PieceColor.QUEEN, cx, cy)
    if (step < 2) return out

    val innerColors = listOf(
        PieceColor.WHITE, PieceColor.BLACK,
        PieceColor.WHITE, PieceColor.BLACK,
        PieceColor.WHITE, PieceColor.BLACK
    )
    repeat(6) { i ->
        val angle = Math.toRadians(i * 60.0 - 90.0)
        out += CarromPiece(
            color = innerColors[i],
            xFraction = cx + (r1 * cos(angle)).toFloat(),
            yFraction = cy + (r1 * sin(angle)).toFloat()
        )
    }
    if (step < 3) return out

    val yArmOuterIndices = setOf(0, 4, 8)
    yArmOuterIndices.forEach { i ->
        val angle = Math.toRadians(i * 30.0 - 90.0)
        out += CarromPiece(
            color = PieceColor.WHITE,
            xFraction = cx + (r2 * cos(angle)).toFloat(),
            yFraction = cy + (r2 * sin(angle)).toFloat()
        )
    }
    if (step < 4) return out

    val fillColors = listOf(
        PieceColor.BLACK, PieceColor.BLACK, PieceColor.WHITE,
        PieceColor.BLACK, PieceColor.BLACK, PieceColor.WHITE,
        PieceColor.BLACK, PieceColor.BLACK, PieceColor.WHITE
    )
    var fillIdx = 0
    for (i in 0 until 12) {
        if (i in yArmOuterIndices) continue
        val angle = Math.toRadians(i * 30.0 - 90.0)
        out += CarromPiece(
            color = fillColors[fillIdx],
            xFraction = cx + (r2 * cos(angle)).toFloat(),
            yFraction = cy + (r2 * sin(angle)).toFloat()
        )
        fillIdx++
    }
    return out
}

@Composable
internal fun animationsEnabledOnDevice(): Boolean {
    val context = LocalContext.current
    return remember {
        runCatching {
            Settings.Global.getFloat(
                context.contentResolver,
                Settings.Global.ANIMATOR_DURATION_SCALE,
                1f
            ) != 0f
        }.getOrDefault(true)
    }
}
