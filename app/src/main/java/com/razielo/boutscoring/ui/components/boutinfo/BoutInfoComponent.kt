package com.razielo.boutscoring.ui.components.boutinfo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.razielo.boutscoring.R
import com.razielo.boutscoring.data.models.BoutInfo
import com.razielo.boutscoring.data.models.enums.DrawMethod
import com.razielo.boutscoring.data.models.enums.NoResultMethod
import com.razielo.boutscoring.data.models.enums.WeightClass
import com.razielo.boutscoring.data.models.enums.WinMethod
import com.razielo.boutscoring.data.models.enums.Winner

@Composable
fun BoutInfoComponent(boutInfo: BoutInfo, updateInfo: (BoutInfo) -> Unit) {
    var info by remember { mutableStateOf(boutInfo) }
    val resultOptions = Winner.entries.map { it.displayName }
    var methodOptions: List<String>? by remember { mutableStateOf(null) }
    var methodSelected: String? by remember { mutableStateOf(updateMethodSelected(info)) }
    val weights = WeightClass.entries.map { it.displayName }

    var location by remember { mutableStateOf(boutInfo.location) }
    var notes by remember { mutableStateOf(boutInfo.notes) }

    when {
        info.winner != null -> methodOptions = getMethodOptions(info.winner)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        DropdownSelection(
            title = stringResource(R.string.winner_dropdown_label),
            options = resultOptions,
            selected = info.winner?.displayName
        ) { selected ->
            info = updateWinner(info, selected, updateInfo)
            methodSelected = updateMethodSelected(info)
        }

        DropdownSelection(
            title = stringResource(R.string.method_dropdown_label),
            options = methodOptions ?: emptyList(),
            enabled = methodOptions != null,
            selected = methodSelected
        ) { selected ->
            info = updateMethod(info, selected, updateInfo)
            methodSelected = updateMethodSelected(info)
        }

        DropdownSelection(
            title = stringResource(R.string.weight_dropdown_label),
            options = weights,
            selected = info.weight?.displayName
        ) { selected ->
            info = updateWeight(info, selected, updateInfo)
        }

        ChampionshipToggle(info.championship) {
            info = info.copy(championship = it)
            updateInfo(info)
        }

        BoutDateSelector(boutInfo, updateInfo)

        InfoTextField(
            text = stringResource(R.string.location),
            value = location,
            modifier = Modifier.fillMaxWidth()
        ) {
            location = it
            info = info.copy(location = location)
            updateInfo(info)
        }

        InfoTextField(
            text = stringResource(R.string.notes), value = notes, modifier = Modifier.fillMaxSize()
        ) {
            notes = it
            info = info.copy(notes = notes)
            updateInfo(info)
        }
    }
}

@Composable
private fun InfoTextField(
    text: String, value: String, modifier: Modifier, onValueChange: (String) -> Unit
) {
    var focused by remember { mutableStateOf(false) }

    Column {
        if (value.isBlank() && !focused) {
            Text(text)
        }
        OutlinedTextField(
            value,
            label = { Text(text) },
            keyboardOptions = KeyboardOptions.Default.copy(
                capitalization = KeyboardCapitalization.Sentences,
                keyboardType = KeyboardType.Text
            ),
            modifier = modifier.onFocusChanged { focused = it.isFocused },
            onValueChange = onValueChange
        )
    }
}

@Composable
private fun ChampionshipToggle(championship: Boolean, toggle: (Boolean) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(stringResource(R.string.championship_in_play), Modifier.padding(end = 16.dp))
        Switch(championship, onCheckedChange = toggle)
    }
}


private fun updateMethodSelected(info: BoutInfo): String? = when (info.winner) {
    Winner.BLUE_CORNER, Winner.RED_CORNER -> info.winMethod?.displayName
    Winner.DRAW -> info.drawMethod?.displayName
    Winner.NO_RESULT -> info.noResultMethod?.displayName
    null -> null
}

private fun updateWeight(info: BoutInfo, selection: String, update: (BoutInfo) -> Unit): BoutInfo {
    val weight = WeightClass.fromDisplayName(selection)
    val newInfo = info.copy(weight = weight)
    update(newInfo)
    return newInfo
}

private fun updateWinner(info: BoutInfo, selection: String, update: (BoutInfo) -> Unit): BoutInfo {
    val winner = Winner.fromDisplayName(selection)
    val newInfo =
        info.copy(winner = winner, winMethod = null, drawMethod = null, noResultMethod = null)
    update(newInfo)
    return newInfo
}

private fun updateMethod(info: BoutInfo, option: String, update: (BoutInfo) -> Unit): BoutInfo {
    val newInfo = when (info.winner) {
        null -> {
            info
        }

        Winner.RED_CORNER, Winner.BLUE_CORNER -> {
            info.copy(winMethod = WinMethod.fromDisplayName(option))
        }

        Winner.DRAW -> {
            info.copy(drawMethod = DrawMethod.fromDisplayName(option))
        }

        Winner.NO_RESULT -> {
            info.copy(noResultMethod = NoResultMethod.fromDisplayName(option))
        }
    }

    update(newInfo)
    return newInfo
}

private fun getMethodOptions(winner: Winner?): List<String> = when (winner) {
    Winner.RED_CORNER, Winner.BLUE_CORNER -> WinMethod.entries.map { it.displayName }
    Winner.DRAW -> DrawMethod.entries.map { it.displayName }
    Winner.NO_RESULT -> NoResultMethod.entries.map { it.displayName }
    null -> emptyList()
}