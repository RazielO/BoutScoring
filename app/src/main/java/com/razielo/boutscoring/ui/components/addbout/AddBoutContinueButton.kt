package com.razielo.boutscoring.ui.components.addbout

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.razielo.boutscoring.data.models.Bout
import com.razielo.boutscoring.data.models.Fighter
import kotlinx.coroutines.launch

@Composable
fun AddBoutContinueButton(
    snackbarHostState: SnackbarHostState,
    redCornerValues: List<String>,
    blueCornerValues: List<String>,
    roundsIndex: Int,
    roundsValues: List<Int>,
    goToScore: (Bout) -> Unit
) {
    val scope = rememberCoroutineScope()

    Button(
        onClick = {
            if (redCornerValues.any { it.isBlank() }) {
                scope.launch {
                    snackbarHostState.showSnackbar("Fill the red corner info first")
                }
            } else if (blueCornerValues.any { it.isBlank() }) {
                scope.launch {
                    snackbarHostState.showSnackbar("Fill the blue corner info first")
                }
            } else if (roundsIndex == -1) {
                scope.launch {
                    snackbarHostState.showSnackbar("Select the number of rounds first")
                }
            } else {
                val rounds = roundsValues[roundsIndex]
                val scores: Map<Int, Pair<Int, Int>> = (1 .. rounds).associateWith { Pair(0, 0) }
                val redCorner = Fighter(
                    fullName = redCornerValues[0].trim(),
                    displayName = redCornerValues[1].trim()
                )
                val blueCorner = Fighter(
                    fullName = blueCornerValues[0].trim(),
                    displayName = blueCornerValues[1].trim()
                )
                val bout = Bout(
                    rounds = rounds,
                    scores = scores,
                    redCorner = redCornerValues[0].trim(),
                    blueCorner = blueCornerValues[0].trim()
                )

                goToScore(bout)
            }
        },
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text("Continue")
    }
}
