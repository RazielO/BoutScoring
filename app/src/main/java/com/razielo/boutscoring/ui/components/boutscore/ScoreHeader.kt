package com.razielo.boutscoring.ui.components.boutscore

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.razielo.boutscoring.data.models.Bout
import com.razielo.boutscoring.scoreColors
import com.razielo.boutscoring.ui.components.common.BoutScoreResult

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
        val colors = scoreColors(Pair(redScore, blueScore), MaterialTheme.colorScheme.onBackground)

        HeadText(redScore.toString(), Modifier.weight(2f), colors.first)
        BoutScoreResult(bout, Modifier.weight(1f))
        HeadText(blueScore.toString(), Modifier.weight(2f), colors.second)
    }
}

@Composable
fun HeadText(
    text: String,
    modifier: Modifier,
    color: Color = MaterialTheme.colorScheme.onBackground
) {
    val textColor = color

    Text(
        text,
        textAlign = TextAlign.Center,
        modifier = modifier,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        color = textColor
    )
}
