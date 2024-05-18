package com.razielo.boutscoring.ui.components.boutscore

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.razielo.boutscoring.data.models.Bout
import com.razielo.boutscoring.scoreColors
import com.razielo.boutscoring.ui.models.ParsedBout
import com.razielo.boutscoring.ui.theme.Blue
import com.razielo.boutscoring.ui.theme.Red

@Composable
fun BoutScoreComponent(
    snackbarHostState: SnackbarHostState,
    boutParam: ParsedBout,
    update: (ParsedBout) -> Unit,
) {
    var bout by remember { mutableStateOf(boutParam) }
    val brush = Brush.horizontalGradient(listOf(Red, Blue))

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        HeaderNames(bout.redCorner.displayName, bout.blueCorner.displayName, bout.info.championship)
        HeaderScores(bout.bout, bout.info)
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(bout.bout.rounds) { round ->
                val enabled = isRoundEnabled(bout.bout, round)
                val colors: Pair<Color, Color> = scoreColors(
                    bout.bout.scores[round + 1] ?: Pair(0, 0),
                    MaterialTheme.colorScheme.onBackground
                )

                RoundRow(snackbarHostState = snackbarHostState,
                    brush = brush,
                    enabled = enabled,
                    round = round + 1,
                    scores = bout.bout.scores[round + 1] ?: Pair(0, 0),
                    colors = colors,
                    updateRound = { i, values ->
                        bout = bout.copy(bout = updateRound(bout.bout, i, values))
                        update(bout)
                    })
            }
        }
    }
}

private fun updateRound(bout: Bout, round: Int, values: Pair<Int, Int>): Bout =
    bout.copy(scores = bout.scores.toMutableMap().apply { set(round, values) })

private fun isRoundEnabled(bout: Bout, round: Int): Boolean =
    round == 0 || (bout.scores[round]?.first != 0 && bout.scores[round]?.second != 0)
