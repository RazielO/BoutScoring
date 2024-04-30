package com.razielo.boutscoring.ui.components.boutscore

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.razielo.boutscoring.data.models.Bout
import com.razielo.boutscoring.data.models.DrawMethod
import com.razielo.boutscoring.data.models.NoResultMethod
import com.razielo.boutscoring.data.models.WinMethod
import com.razielo.boutscoring.data.models.Winner
import com.razielo.boutscoring.ui.theme.Green
import com.razielo.boutscoring.ui.theme.Red

@Composable
fun HeaderNames(redCorner: String, blueCorner: String) {
    Row {
        HeadText(redCorner, Modifier.weight(2f))
        HeadText("vs", Modifier.weight(1f))
        HeadText(blueCorner, Modifier.weight(2f))
    }
}

@Composable
fun HeaderScores(bout: Bout) {
    val redScore = bout.scores.values.sumOf { it.first }
    val blueScore = bout.scores.values.sumOf { it.second }

    Row {
        val colors = scoreColors(redScore, blueScore, MaterialTheme.colorScheme.onBackground)

        HeadText(redScore.toString(), Modifier.weight(2f), colors.first)
        BoutResultText(bout, Modifier.weight(1f))
        HeadText(blueScore.toString(), Modifier.weight(2f), colors.second)
    }
}

@Composable
fun HeadText(
    text: String, modifier: Modifier, color: Color = MaterialTheme.colorScheme.onBackground
) {
    Text(
        text,
        textAlign = TextAlign.Center,
        modifier = modifier,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        color = color
    )
}

@Composable
private fun BoutResultText(bout: Bout, modifier: Modifier) {
    val text = when (bout.winner) {
        null -> ""
        Winner.RED_CORNER, Winner.BLUE_CORNER -> resultText(bout.winner, bout.winMethod)
        Winner.DRAW -> resultText(bout.winner, bout.drawMethod)
        Winner.NO_RESULT -> resultText(bout.winner, bout.noResultMethod)
    }
    val color = resultColor(bout.winner) ?: MaterialTheme.colorScheme.background

    Box(
        modifier = modifier.clip(RoundedCornerShape(8.dp))
    ) {
        HeadText(
            text,
            Modifier
                .fillMaxWidth()
                .background(color)
        )
    }
}

private fun resultText(winner: Winner?, method: Any?): String {
    val prefix = when (winner) {
        null, Winner.DRAW, Winner.NO_RESULT -> ""
        Winner.RED_CORNER -> "W"
        Winner.BLUE_CORNER -> "L"
    }

    val text = when (winner) {
        null -> ""
        else -> when (method) {
            null -> ""
            is WinMethod -> "-${method.abbreviation}"
            is DrawMethod -> method.abbreviation
            is NoResultMethod -> method.abbreviation
            else -> ""
        }
    }

    return "$prefix$text"
}

private fun resultColor(winner: Winner?): Color? = when (winner) {
    null -> null
    Winner.RED_CORNER -> Green
    Winner.BLUE_CORNER -> Red
    Winner.DRAW, Winner.NO_RESULT -> Color.LightGray
}

private fun scoreColors(redScore: Int, blueScore: Int, default: Color): Pair<Color, Color> =
    if (redScore > blueScore) {
        Pair(Green, Red)
    } else if (blueScore > redScore) {
        Pair(Red, Green)
    } else {
        Pair(default, default)
    }