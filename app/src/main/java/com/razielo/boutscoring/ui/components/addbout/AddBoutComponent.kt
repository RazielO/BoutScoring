package com.razielo.boutscoring.ui.components.addbout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.razielo.boutscoring.R
import com.razielo.boutscoring.data.models.enums.WeightClass
import com.razielo.boutscoring.data.models.enums.WeightClass.Companion.displayName
import com.razielo.boutscoring.ui.components.boutinfo.DropdownSelection
import com.razielo.boutscoring.ui.components.common.ChampionshipToggle
import com.razielo.boutscoring.ui.models.ParsedBout
import com.razielo.boutscoring.ui.theme.BoutScoringTheme
import com.razielo.boutscoring.ui.theme.blueContainerDark
import com.razielo.boutscoring.ui.theme.redContainerDark
import kotlinx.coroutines.launch

@Composable
fun AddBoutComponent(
    snackbarHostState: SnackbarHostState, goToBoutScore: (ParsedBout) -> Unit
) {
    val scope = rememberCoroutineScope()
    val fillAllFields = stringResource(R.string.fill_all_fields)
    val selectRoundsFirst = stringResource(R.string.select_rounds_first)

    var redCornerValues by remember { mutableStateOf(List(2) { "" }) }
    var blueCornerValues by remember { mutableStateOf(List(2) { "" }) }
    var selectedRoundsIndex by remember { mutableIntStateOf(-1) }
    val rounds: List<Int> = listOf(3, 4, 5, 6, 8, 10, 12, 15)
    var championship by remember { mutableStateOf(false) }

    val weights = WeightClass.entries
    var selectedWeightIndex: Int? by remember { mutableStateOf(null) }


    val redCornerUpdate: (Int, String) -> Unit =
        { i, value -> redCornerValues = updateCornerValues(redCornerValues, i, value) }
    val blueCornerUpdate: (Int, String) -> Unit =
        { i, value -> blueCornerValues = updateCornerValues(blueCornerValues, i, value) }

    val onContinue: () -> Unit = {
        if (redCornerValues.any { it.isBlank() } || blueCornerValues.any { it.isBlank() }) {
            scope.launch {
                snackbarHostState.showSnackbar(fillAllFields)
            }
        } else if (selectedRoundsIndex == -1) {
            scope.launch {
                snackbarHostState.showSnackbar(selectRoundsFirst)
            }
        } else {
            val weightClass = selectedWeightIndex?.let { weights[it] }
            val bout = ParsedBout.fromInputData(
                rounds[selectedRoundsIndex],
                redCornerValues[0],
                redCornerValues[1],
                blueCornerValues[0],
                blueCornerValues[1],
                championship,
                weightClass
            )

            goToBoutScore(bout)
        }
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AddBoutInputFieldGroup(
            redCornerValues,
            stringResource(R.string.red_corner_full_name),
            redContainerDark,
            redCornerUpdate
        )

        Text(
            stringResource(R.string.vs).uppercase(),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic,
            color = Color.Gray
        )

        AddBoutInputFieldGroup(
            blueCornerValues,
            stringResource(R.string.blue_corner_full_name),
            blueContainerDark,
            blueCornerUpdate
        )

        Spacer(Modifier.height(16.dp))

        Text(
            stringResource(R.string.bout_settings).uppercase(),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.75f)
        )

        HorizontalDivider()

        ChampionshipToggle(championship) { championship = !championship }

        Spacer(Modifier.height(8.dp))

        DropdownSelection(
            label = stringResource(R.string.weight_dropdown_label),
            hintText = stringResource(R.string.select_weight_class),
            options = weights.map { it.displayName() },
            selectedIndex = selectedWeightIndex
        ) {
            selectedWeightIndex = it
        }

        Spacer(Modifier.height(8.dp))

        RoundSelector(
            rounds, selectedRoundsIndex
        ) { selectedRoundsIndex = it }

        Spacer(Modifier.height(8.dp))


        ElevatedButton(
            onClick = onContinue,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            colors = ButtonDefaults.buttonColors(),
            elevation = ButtonDefaults.buttonElevation(8.dp)
        ) {
            Text(
                stringResource(R.string.continue_text),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun RoundSelector(
    rounds: List<Int>,
    selectedRoundsIndex: Int,
    onSelectedRoundsIndexChange: (Int) -> Unit
) {
    Column {
        Text(
            stringResource(R.string.number_of_rounds),
            style = MaterialTheme.typography.labelLarge,
        )

        ButtonGroup(
            rounds,
            selectedRoundsIndex,
            onSelectedRoundsIndexChange
        )
    }
}

private fun updateCornerValues(
    values: List<String>,
    index: Int, newValue: String
): List<String> = if (index == 1) {
    updateDisplayName(values, newValue)
} else {
    updateFullName(values, newValue)
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

@Preview(showBackground = true)
@Composable
private fun AddBoutComponentPreview() {
    val snackbarHostState = remember { SnackbarHostState() }
    BoutScoringTheme {
        AddBoutComponent(snackbarHostState) { }
    }
}