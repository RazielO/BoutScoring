package com.razielo.boutscoring.ui.components.main

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.razielo.boutscoring.ui.models.ParsedBout
import com.razielo.boutscoring.ui.theme.AppColors
import kotlin.math.sign

/**
 * Helper class to map each round and cumulative delta
 */
private data class CumulativePoint(
    val round: Int,
    val total: Int,
    val delta: Int,
    val scored: Boolean
)

/**
 * Line graph where the X-axis represent the rounds and the Y-axis the cumulative deltas,
 * each point is colored according to who won it and the line according to who is winning the fight
 */
@Composable
fun CumulativeScoreGraph(
    scores: Map<Int, Pair<Int, Int>>,
    modifier: Modifier = Modifier
) {
    val data = cumulativeDelta(scores)

    if (data.size < 2) return

    val maxY = data.maxOf { it.total }.coerceAtLeast(1)
    val minY = data.minOf { it.total }.coerceAtMost(-1)
    val yRange = (maxY - minY).coerceAtLeast(1)

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .padding(horizontal = 16.dp)
    ) {
        val xStep = size.width / (data.size - 1)
        val yScale = size.height / yRange.toFloat()

        fun yPos(value: Int): Float =
            size.height - (value - minY) * yScale

        val zeroY = yPos(0)

        // Zero line
        drawLine(
            color = Color.Gray.copy(alpha = 0.3f),
            start = Offset(0f, zeroY),
            end = Offset(size.width, zeroY),
            strokeWidth = 4f,
        )

        // Draw line segments according to who is winning the fight
        for (i in 1 until data.size) {
            val prev = data[i - 1]
            val curr = data[i]

            if (!curr.scored) {
                continue
            }

            val x1 = (i - 1) * xStep
            val x2 = i * xStep
            val y1 = yPos(prev.total)
            val y2 = yPos(curr.total)

            val lineColor = when {
                curr.total == 0 -> Color.Gray
                curr.total > 0 -> AppColors.Red
                else -> AppColors.Blue
            }

            // Color the line as accumulated delta, to show bout's story
            if (prev.total.sign == curr.total.sign || prev.total == 0) {
                drawLine(
                    color = lineColor,
                    start = Offset(x1, y1),
                    end = Offset(x2, y2),
                    strokeWidth = 8f,
                    cap = StrokeCap.Round
                )
            } else {
                // Split at zero
                val t = zeroCrossFraction(prev.total, curr.total)
                val xZero = x1 + (x2 - x1) * t

                val firstLineColor = when {
                    prev.total > 0 -> AppColors.Red
                    else -> AppColors.Blue
                }

                // First half
                drawLine(
                    color = firstLineColor,
                    start = Offset(x1, y1),
                    end = Offset(xZero, zeroY),
                    strokeWidth = 8f,
                    cap = StrokeCap.Round
                )

                // Second half
                drawLine(
                    color = lineColor,
                    start = Offset(xZero, zeroY),
                    end = Offset(x2, y2),
                    strokeWidth = 8f,
                    cap = StrokeCap.Round
                )
            }
        }

        // Color each point based on who won the round
        data.forEachIndexed { index, point ->
            if (!point.scored) {
                return@forEachIndexed
            }

            val pointColor = when {
                point.delta == 0 -> Color.Gray
                point.delta > 0 -> AppColors.Red
                else -> AppColors.Blue
            }

            drawCircle(
                color = pointColor,
                radius = 8f,
                center = Offset(index * xStep, yPos(point.total))
            )
        }
    }
}

/**
 * Calculates the fraction along a line segment where the zero crossing occurs.
 */
private fun zeroCrossFraction(y1: Int, y2: Int): Float {
    return y1.toFloat() / (y1 - y2).toFloat()
}

/**
 * Iterate over all scores and calculate the cumulative and each round's deltas
 */
private fun cumulativeDelta(
    scores: Map<Int, Pair<Int, Int>>
): List<CumulativePoint> {
    var total = 0

    return scores.toSortedMap().map { (round, score) ->
        val (a, b) = score

        // Unscored round
        if (a == 0 && b == 0) {
            CumulativePoint(round, total, 0, false)
        } else {
            val delta = a - b
            total += delta
            CumulativePoint(round, total, delta, true)
        }
    }
}

/**
 * Graph preview
 */
@Preview(showBackground = true)
@Composable
fun CumulativeScoreGraphPreview() {
    CumulativeScoreGraph(ParsedBout.example().bout.scores)
}