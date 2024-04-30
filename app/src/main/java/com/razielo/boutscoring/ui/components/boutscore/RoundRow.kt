package com.razielo.boutscoring.ui.components.boutscore

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch


@Composable
fun RoundRow(
    snackbarHostState: SnackbarHostState,
    brush: Brush,
    enabled: Boolean,
    round: Int,
    scores: Pair<Int, Int>,
    colors: Pair<Color?, Color?>,
    updateRound: (Int, Pair<Int, Int>) -> Unit
) {
    val scope = rememberCoroutineScope()
    val redRoundScore = scores.first
    val blueRoundScore = scores.second

    fun tryToUpdate(red: Boolean) {
        if (enabled) {
            updateRound(round, roundScore(scores, red))
        } else {
            scope.launch {
                snackbarHostState.showSnackbar("Score the previous round first")
            }
        }
    }

    Row(
        Modifier
            .border(1.dp, brush, RoundedCornerShape(8.dp))
            .padding(vertical = 0.dp)
    ) {
        RoundRowButton(Modifier.weight(2f), redRoundScore, colors.first) { tryToUpdate(true) }
        RoundRowText(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically), value = round
        )
        RoundRowButton(Modifier.weight(2f), blueRoundScore, colors.second) { tryToUpdate(false) }
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
private fun RoundRowButton(modifier: Modifier, value: Int, textColor: Color?, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier.padding(0.dp),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(
            contentColor = MaterialTheme.colorScheme.onBackground,
            containerColor = MaterialTheme.colorScheme.background
        )
    ) {
        RoundRowText(Modifier.padding(0.dp), value, textColor)
    }
}

@Composable
private fun RoundRowText(modifier: Modifier, value: Int, textColor: Color? = null) {
    val color = textColor ?: MaterialTheme.colorScheme.onBackground

    Text(
        value.toString(),
        textAlign = TextAlign.Center,
        color = color,
        modifier = modifier.padding(0.dp),
        fontSize = MaterialTheme.typography.bodyLarge.fontSize
    )
}
