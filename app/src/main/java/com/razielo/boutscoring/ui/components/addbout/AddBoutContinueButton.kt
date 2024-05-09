package com.razielo.boutscoring.ui.components.addbout

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.razielo.boutscoring.R
import com.razielo.boutscoring.data.models.Bout
import com.razielo.boutscoring.data.models.BoutWithFighters
import com.razielo.boutscoring.data.models.Fighter
import kotlinx.coroutines.launch

@Composable
fun AddBoutContinueButton(
    snackbarHostState: SnackbarHostState,
    redCornerValues: List<String>,
    blueCornerValues: List<String>,
    roundsIndex: Int,
    roundsValues: List<Int>,
    goToScore: (BoutWithFighters) -> Unit
) {
    val scope = rememberCoroutineScope()
    val messageText: String = when {
        redCornerValues.any { it.isBlank() } -> "Fill the red corner info first"
        blueCornerValues.any { it.isBlank() } -> "Fill the blue corner info first"
        roundsIndex == -1 -> "Select the number of rounds first"
        else -> ""
    }

    Button(
        onClick = {
            if (messageText.isNotBlank()) {
                scope.launch {
                    snackbarHostState.showSnackbar(messageText)
                }
            } else {
                val bout = createBout(roundsValues[roundsIndex], redCornerValues, blueCornerValues)
                goToScore(bout)
            }
        },
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(stringResource(R.string.continue_text))
    }
}

private fun createBout(
    rounds: Int, redCornerValues: List<String>, blueCornerValues: List<String>
): BoutWithFighters {
    val scores: Map<Int, Pair<Int, Int>> = (1 .. rounds).associateWith { Pair(0, 0) }
    val redCorner =
        Fighter(fullName = redCornerValues[0].trim(), displayName = redCornerValues[1].trim())
    val blueCorner =
        Fighter(fullName = blueCornerValues[0].trim(), displayName = blueCornerValues[1].trim())
    val bout = Bout(rounds = rounds, scores = scores)

    return BoutWithFighters(bout, listOf(redCorner, blueCorner))
}
