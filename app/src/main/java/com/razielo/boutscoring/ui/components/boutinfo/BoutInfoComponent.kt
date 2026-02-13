package com.razielo.boutscoring.ui.components.boutinfo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.razielo.boutscoring.R
import com.razielo.boutscoring.data.models.BoutInfo
import com.razielo.boutscoring.data.models.enums.DrawMethod
import com.razielo.boutscoring.data.models.enums.NoResultMethod
import com.razielo.boutscoring.data.models.enums.WeightClass
import com.razielo.boutscoring.data.models.enums.WinMethod
import com.razielo.boutscoring.data.models.enums.Winner
import com.razielo.boutscoring.ui.components.common.CardStyleTextField
import com.razielo.boutscoring.ui.components.common.ChampionshipToggle
import com.razielo.boutscoring.ui.models.ParsedBout

@Composable
fun BoutInfoComponent(boutInfo: BoutInfo, updateInfo: (BoutInfo) -> Unit) {
    var info by remember { mutableStateOf(boutInfo) }
    var methodOptions: List<String>? by remember { mutableStateOf(null) }
    var methodSelected: String? by remember { mutableStateOf(updateMethodSelected(info)) }
    val weights = WeightClass.entries.map { it.displayName }
    val resultOptions = Winner.entries.map { it.displayName }

    var location by remember { mutableStateOf(boutInfo.location) }
    var notes by remember { mutableStateOf(boutInfo.notes) }

    when {
        info.winner != null -> methodOptions = getMethodOptions(info.winner)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        DropdownSelection(
            title = stringResource(R.string.winner_dropdown_label),
            options = resultOptions,
            selectedElement = info.winner?.displayName,
        ) {
            methodSelected = null
            info = updateWinner(info, it, updateInfo)
            methodOptions = getMethodOptions(info.winner)
        }

        DropdownSelection(
            title = stringResource(R.string.method_dropdown_label),
            options = methodOptions ?: emptyList(),
            enabled = methodOptions != null,
            selectedElement = methodSelected
        ) {
            info = updateMethod(info, it, updateInfo)
            methodSelected = updateMethodSelected(info)
        }

        DropdownSelection(
            title = stringResource(R.string.weight_dropdown_label),
            options = weights,
            selectedElement = info.weight?.displayName,
        ) {
            info = updateWeight(info, it, updateInfo)
        }

        ChampionshipToggle(info.championship) {
            info = info.copy(championship = it)
            updateInfo(info)
        }

        BoutDateSelector(boutInfo) {
            info = info.copy(date = it)
            updateInfo(info)
        }

        CardStyleTextField(
            hintText = stringResource(R.string.location),
            labelText = stringResource(R.string.location_label),
            value = location,
            modifier = Modifier.fillMaxWidth()
        ) {
            location = it
            info = info.copy(location = location)
            updateInfo(info)
        }

        CardStyleTextField(
            hintText = stringResource(R.string.notes),
            labelText = stringResource(R.string.notes_label),
            value = notes,
            modifier = Modifier
                .defaultMinSize(minHeight = 200.dp)
                .fillMaxWidth()
        ) {
            notes = it
            info = info.copy(notes = notes)
            updateInfo(info)
        }
    }
}

private fun updateMethodSelected(info: BoutInfo): String? = when (info.winner) {
    Winner.BLUE_CORNER, Winner.RED_CORNER -> info.winMethod?.displayName
    Winner.DRAW -> info.drawMethod?.displayName
    Winner.NO_RESULT -> info.noResultMethod?.displayName
    null -> null
}

private fun updateWeight(info: BoutInfo, selection: String?, update: (BoutInfo) -> Unit): BoutInfo {
    if (selection != null) {
        val weight = WeightClass.fromDisplayName(selection)
        val newInfo = info.copy(weight = weight)
        update(newInfo)
        return newInfo
    } else {
        return info
    }
}

private fun updateWinner(info: BoutInfo, selection: String?, update: (BoutInfo) -> Unit): BoutInfo {
    if (selection != null) {
        val winner = Winner.fromDisplayName(selection)
        val newInfo =
            info.copy(winner = winner, winMethod = null, drawMethod = null, noResultMethod = null)
        update(newInfo)
        return newInfo
    } else {
        return info
    }
}

private fun updateMethod(info: BoutInfo, option: String?, update: (BoutInfo) -> Unit): BoutInfo {
    if (option != null) {
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
    } else {
        return info
    }
}

private fun getMethodOptions(winner: Winner?): List<String> = when (winner) {
    Winner.RED_CORNER, Winner.BLUE_CORNER -> WinMethod.entries.map { it.displayName }
    Winner.DRAW -> DrawMethod.entries.map { it.displayName }
    Winner.NO_RESULT -> NoResultMethod.entries.map { it.displayName }
    null -> emptyList()
}

@Preview(showBackground = true)
@Composable
private fun BoutInfoComponentPreview() {
    BoutInfoComponent(ParsedBout.example().info.copy()) { }
}
