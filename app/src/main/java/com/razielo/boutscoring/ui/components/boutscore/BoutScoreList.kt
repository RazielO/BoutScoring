package com.razielo.boutscoring.ui.components.boutscore

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.razielo.boutscoring.R
import com.razielo.boutscoring.data.models.Bout
import com.razielo.boutscoring.scoreColors
import com.razielo.boutscoring.ui.models.ParsedBout
import com.razielo.boutscoring.ui.theme.BoutScoringTheme
import com.razielo.boutscoring.ui.theme.blueContainerDark
import com.razielo.boutscoring.ui.theme.redContainerDark
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Component for scoring a bout displayed as a list of rounds
 */
@Composable
fun BoutScoreList(
    boutParam: ParsedBout,
    showErrorSnackbar: () -> Job,
    update: (ParsedBout) -> Unit,
) {
    var bout by remember { mutableStateOf(boutParam) }
    val brush = Brush.horizontalGradient(listOf(redContainerDark, blueContainerDark))


    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(bout.bout.rounds) { round ->
            val enabled = isRoundEnabled(bout.bout, round)
            val colors: Pair<Color, Color> = scoreColors(
                bout.bout.scores[round + 1] ?: Pair(0, 0)
            )

            RoundRow(
                brush = brush,
                enabled = enabled,
                round = round + 1,
                scores = bout.bout.scores[round + 1] ?: Pair(0, 0),
                colors = colors,
                showErrorSnackbar = showErrorSnackbar,
                updateRound = { i, values ->
                    bout = bout.copy(bout = updateRound(bout.bout, i, values))
                    update(bout)
                }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun RoundRow(
    brush: Brush,
    enabled: Boolean,
    round: Int,
    scores: Pair<Int, Int>,
    colors: Pair<Color, Color>,
    showErrorSnackbar: () -> Job,
    updateRound: (Int, Pair<Int, Int>) -> Unit
) {
    val redRoundScore = scores.first
    val blueRoundScore = scores.second

    fun tryToUpdate(red: Boolean, reset: Boolean = false) {
        if (enabled) {
            if (reset) {
                updateRound(round, Pair(0, 0))
            } else {
                updateRound(round, roundScore(scores, red))
            }
        } else {
            showErrorSnackbar()
        }
    }

    Card(
        elevation = CardDefaults.elevatedCardElevation(8.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, brush, RoundedCornerShape(8.dp))
    ) {
        Row(
            Modifier
                .combinedClickable(onLongClick = { tryToUpdate(true, reset = true) }, onClick = {})
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            RoundRowButton(Modifier.weight(2f), redRoundScore, colors.first) { tryToUpdate(true) }

            Text(
                stringResource(R.string.round_i, round).uppercase(),
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                color = Color.Gray,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold
            )

            RoundRowButton(
                Modifier.weight(2f),
                blueRoundScore,
                colors.second
            ) { tryToUpdate(false) }
        }
    }
}

private fun <T, U> Pair<T, U>.swap(): Pair<U, T> = Pair(this.second, this.first)

private fun roundScore(scores: Pair<Int, Int>, red: Boolean): Pair<Int, Int> {
    val newScores = with(if (red) scores else scores.swap()) {
        if (this.first == 0 && this.second == 0) {
            Pair(10, 9)
        } else if (this.first == 6) {
            this.copy(first = 10)
        } else {
            this.copy(first = this.first - 1)
        }
    }

    return if (!red) newScores.swap() else newScores
}

@Composable
private fun RoundRowButton(modifier: Modifier, value: Int, textColor: Color, onClick: () -> Unit) {
    TextButton(
        onClick = onClick,
        modifier = modifier.padding(0.dp),
        contentPadding = PaddingValues(0.dp),
    ) {
        Text(
            value.toString(),
            textAlign = TextAlign.Center,
            color = textColor,
            modifier = modifier.padding(0.dp),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
    }
}


private fun updateRound(bout: Bout, round: Int, values: Pair<Int, Int>): Bout =
    bout.copy(scores = bout.scores.toMutableMap().apply { set(round, values) })

private fun isRoundEnabled(bout: Bout, round: Int): Boolean =
    round == 0 || (bout.scores[round]?.first != 0 && bout.scores[round]?.second != 0)

@Preview(showBackground = true)
@Composable
private fun BoutScoreListPreview() {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val errorMsg = stringResource(R.string.score_round_error)

    BoutScoringTheme {
        BoutScoreList(
            ParsedBout.example(),
            {

                scope.launch {
                    snackbarHostState.showSnackbar(errorMsg)
                }
            }
        ) { }
    }
}