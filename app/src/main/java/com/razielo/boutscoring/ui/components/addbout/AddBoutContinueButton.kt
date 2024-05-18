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
import com.razielo.boutscoring.data.models.BoutInfo
import com.razielo.boutscoring.data.models.Fighter
import com.razielo.boutscoring.ui.models.ParsedBout
import kotlinx.coroutines.launch

@Composable
fun AddBoutContinueButton(
    snackbarHostState: SnackbarHostState,
    redCornerValues: List<String>,
    blueCornerValues: List<String>,
    roundsIndex: Int,
    roundsValues: List<Int>,
    goToScore: (ParsedBout) -> Unit
) {
    val scope = rememberCoroutineScope()
    val messageText: String = when {
        redCornerValues.any { it.isBlank() } -> stringResource(
            R.string.missing_info_error, stringResource(R.string.red).lowercase()
        )

        blueCornerValues.any { it.isBlank() } -> stringResource(
            R.string.missing_info_error, stringResource(R.string.blue).lowercase()
        )

        roundsIndex == -1 -> stringResource(R.string.missing_rounds_error)
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
): ParsedBout {
    val scores: Map<Int, Pair<Int, Int>> = (1 .. rounds).associateWith { Pair(0, 0) }
    val redCorner = Fighter(redCornerValues[0].trim(), redCornerValues[1].trim())
    val blueCorner = Fighter(blueCornerValues[0].trim(), blueCornerValues[1].trim())
    val info = BoutInfo()
    val bout = Bout(
        rounds = rounds,
        scores = scores,
        boutInfoId = info.id,
        redCornerId = redCorner.fullName,
        blueCornerId = blueCorner.fullName
    )

    return ParsedBout(bout, info, redCorner, blueCorner)
}
