package com.razielo.boutscoring.ui.components.addbout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.razielo.boutscoring.data.models.BoutWithFighters

@Composable
fun AddBoutComponent(
    snackbarHostState: SnackbarHostState,
    goToBoutScore: (BoutWithFighters) -> Unit
) {
    var redCornerValues by remember { mutableStateOf(List(2) { "" }) }
    var blueCornerValues by remember { mutableStateOf(List(2) { "" }) }
    var selectedButtonIndex by remember { mutableIntStateOf(-1) }
    val rounds: List<Int> = listOf(3, 4, 5, 6, 8, 10, 12, 15)

    Column(modifier = Modifier.padding(16.dp)) {
        AddBoutInputFieldGroup(
            redCornerValues, "Red"
        ) { index, newValue ->
            redCornerValues = updateCornerValues(redCornerValues, index, newValue)
        }

        Text(
            text = "vs",
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            textAlign = TextAlign.Center
        )

        AddBoutInputFieldGroup(
            blueCornerValues, "Blue"
        ) { index, newValue ->
            blueCornerValues = updateCornerValues(blueCornerValues, index, newValue)
        }

        Text(
            text = "Number of rounds",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            textAlign = TextAlign.Center
        )

        ButtonGroup(rounds, { index -> selectedButtonIndex = index }, selectedButtonIndex)

        AddBoutContinueButton(
            snackbarHostState,
            redCornerValues,
            blueCornerValues,
            selectedButtonIndex,
            rounds,
            goToBoutScore
        )
    }
}

private fun updateCornerValues(values: List<String>, index: Int, newValue: String): List<String> {
    return values.toMutableList().apply {
        if (index == 1) {
            set(1, newValue.uppercase())
        } else {
            val parts: List<String> =
                newValue.uppercase().trim().split(" ").filter { it.isNotEmpty() }
            val suffixes: List<String> = listOf("JR", "SR", "II", "III")

            if (parts.last().isNotEmpty() && suffixes.contains(parts.last())) {
                set(1, parts.takeLast(2).joinToString(" "))
            } else {
                set(1, parts.last())
            }

            set(0, newValue.split(" ").joinToString(" ") {
                it.replaceFirstChar { chr -> chr.uppercase() }
            })
        }
    }
}
