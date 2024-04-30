package com.razielo.boutscoring.ui.components.boutscore

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.razielo.boutscoring.data.models.Bout
import com.razielo.boutscoring.ui.theme.Blue
import com.razielo.boutscoring.ui.theme.Green
import com.razielo.boutscoring.ui.theme.Red

@Composable
fun Content(
    snackbarHostState: SnackbarHostState, bout: Bout, updateRound: (Int, Pair<Int, Int>) -> Unit
) {
    val brush = Brush.horizontalGradient(listOf(Red, Blue))

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        HeaderNames(bout.redCorner.displayName, bout.blueCorner.displayName)
        HeaderScores(bout)
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(bout.rounds) { round ->
                val enabled = isRoundEnabled(bout, round)
                val colors: Pair<Color?, Color?> = roundRowColors(bout, round + 1)

                RoundRow(
                    snackbarHostState = snackbarHostState,
                    brush = brush,
                    enabled = enabled,
                    round = round + 1,
                    scores = bout.scores[round + 1] ?: Pair(0, 0),
                    colors = colors,
                    updateRound = updateRound
                )
            }
        }
    }
}

private fun isRoundEnabled(bout: Bout, round: Int): Boolean =
    round == 0 || (bout.scores[round]?.first != 0 && bout.scores[round]?.second != 0)

private fun roundRowColors(bout: Bout, round: Int): Pair<Color?, Color?> =
    with(bout.scores[round]) {
        if (this == null) Pair(null, null)
        else if (this.first > this.second) Pair(Green, Red)
        else if (this.second > this.first) Pair(Red, Green)
        else Pair(null, null)
    }