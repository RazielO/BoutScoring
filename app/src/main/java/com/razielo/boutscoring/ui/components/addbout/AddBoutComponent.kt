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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.razielo.boutscoring.R
import com.razielo.boutscoring.ui.models.ParsedBout

@Composable
fun AddBoutComponent(
    snackbarHostState: SnackbarHostState, goToBoutScore: (ParsedBout) -> Unit
) {
    var redCornerValues by remember { mutableStateOf(List(2) { "" }) }
    var blueCornerValues by remember { mutableStateOf(List(2) { "" }) }
    var selectedButtonIndex by remember { mutableIntStateOf(-1) }
    val rounds: List<Int> = listOf(3, 4, 5, 6, 8, 10, 12, 15)

    val redCornerUpdate: (Int, String) -> Unit =
        { i, value -> redCornerValues = updateCornerValues(redCornerValues, i, value) }
    val blueCornerUpdate: (Int, String) -> Unit =
        { i, value -> blueCornerValues = updateCornerValues(blueCornerValues, i, value) }

    Column(modifier = Modifier.padding(16.dp)) {
        AddBoutInputFieldGroup(
            redCornerValues, stringResource(R.string.red), redCornerUpdate
        )
        TextComponent(
            stringResource(R.string.vs), Modifier.padding(vertical = 8.dp)
        )
        AddBoutInputFieldGroup(
            blueCornerValues, stringResource(R.string.blue), blueCornerUpdate
        )
        TextComponent(
            stringResource(R.string.number_of_rounds), Modifier.padding(top = 24.dp)
        )
        ButtonGroup(
            rounds, { selectedButtonIndex = it }, selectedButtonIndex
        )

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

@Composable
private fun TextComponent(text: String, modifier: Modifier) {
    Text(text, modifier = modifier.fillMaxWidth(), textAlign = TextAlign.Center)
}

private fun updateCornerValues(values: List<String>, index: Int, newValue: String): List<String> {
    return if (index == 1) updateDisplayName(values, newValue) else updateFullName(values, newValue)
}

private fun updateFullName(values: List<String>, value: String): List<String> =
    values.toMutableList().apply {
        val parts: List<String> = value.split(" ")
        val suffixes: List<String> = listOf("JR", "SR", "II", "III")

        if (parts.last().isNotEmpty() && suffixes.contains(parts.last().uppercase())) {
            set(1, parts.takeLast(2).joinToString(" ").uppercase())
        } else if (parts.any { it.isNotBlank() }) {
            set(1, parts.last { it.isNotBlank() }.uppercase())
        } else if (parts.all { it.isBlank() }) {
            set(1, "")
        }

        set(0, parts.joinToString(" ") { it.replaceFirstChar { chr -> chr.uppercase() } })
    }

private fun updateDisplayName(values: List<String>, value: String): List<String> =
    values.toMutableList().apply {
        set(1, value.uppercase())
    }