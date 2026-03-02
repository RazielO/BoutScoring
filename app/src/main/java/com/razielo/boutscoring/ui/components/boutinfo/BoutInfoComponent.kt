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
import com.razielo.boutscoring.data.models.enums.ResultMethod
import com.razielo.boutscoring.data.models.enums.WeightClass
import com.razielo.boutscoring.data.models.enums.WeightClass.Companion.displayName
import com.razielo.boutscoring.data.models.enums.WinMethod
import com.razielo.boutscoring.data.models.enums.Winner
import com.razielo.boutscoring.ui.components.common.CardStyleTextField
import com.razielo.boutscoring.ui.components.common.ChampionshipToggle
import com.razielo.boutscoring.ui.models.ParsedBout
import com.razielo.boutscoring.ui.theme.BoutScoringTheme

@Composable
fun BoutInfoComponent(boutInfo: BoutInfo, updateInfo: (BoutInfo) -> Unit) {
    var info by remember { mutableStateOf(boutInfo) }
    var methodOptions: List<ResultMethod>? by remember { mutableStateOf(null) }

    var methodSelected: Int? by remember { mutableStateOf(null) }
    val weights = WeightClass.entries
    val resultOptions = Winner.entries

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
            options = resultOptions.map { stringResource(it.displayName) },
            selectedIndex = selectedIndex(resultOptions, info.winner),
        ) {
            methodSelected = null
            info = updateWinner(info, it?.let { i -> resultOptions[i] }, updateInfo)
            methodOptions = getMethodOptions(info.winner)
        }

        DropdownSelection(
            title = stringResource(R.string.method_dropdown_label),
            options = methodOptions?.map { stringResource(it.displayName) } ?: emptyList(),
            enabled = methodOptions != null,
            selectedIndex = methodOptions?.let { selectedIndex(it, methodSelected) }
        ) {
            val option = it?.let { i -> methodOptions?.let { list -> list[i] } }
            info = updateMethod(info, option, updateInfo)
            methodSelected = updateMethodSelected(info)
        }

        DropdownSelection(
            title = stringResource(R.string.weight_dropdown_label),
            options = weights.map { it.displayName() },
            selectedIndex = selectedIndex(weights, info.weight),
        ) {
            info = updateWeight(info, it?.let { i -> weights[i] }, updateInfo)
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
            singleLine = true,
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
            singleLine = false,
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

private fun <T> selectedIndex(list: List<T>, element: T?): Int? =
    element?.let { elem -> list.indexOf(elem).takeIf { it >= 0 } }

private fun updateMethodSelected(info: BoutInfo): Int? = when (info.winner) {
    Winner.BLUE_CORNER, Winner.RED_CORNER -> info.winMethod?.displayName
    Winner.DRAW -> info.drawMethod?.displayName
    Winner.NO_RESULT -> info.noResultMethod?.displayName
    null -> null
}

private fun updateWeight(
    info: BoutInfo,
    weight: WeightClass?,
    update: (BoutInfo) -> Unit
): BoutInfo {
    if (weight != null) {
        val newInfo = info.copy(weight = weight)
        update(newInfo)
        return newInfo
    } else {
        return info
    }
}

private fun updateWinner(info: BoutInfo, winner: Winner?, update: (BoutInfo) -> Unit): BoutInfo {
    if (winner != null) {
        val newInfo =
            info.copy(winner = winner, winMethod = null, drawMethod = null, noResultMethod = null)
        update(newInfo)
        return newInfo
    } else {
        return info
    }
}

private fun updateMethod(
    info: BoutInfo,
    option: ResultMethod?,
    update: (BoutInfo) -> Unit
): BoutInfo {
    if (option != null) {
        val newInfo = when (info.winner) {
            null -> {
                info
            }

            Winner.RED_CORNER, Winner.BLUE_CORNER -> {
                info.copy(winMethod = option as WinMethod?)
            }

            Winner.DRAW -> {
                info.copy(drawMethod = option as DrawMethod?)
            }

            Winner.NO_RESULT -> {
                info.copy(noResultMethod = option as NoResultMethod?)
            }
        }

        update(newInfo)
        return newInfo
    } else {
        return info
    }
}

private fun getMethodOptions(winner: Winner?): List<ResultMethod> = when (winner) {
    Winner.RED_CORNER, Winner.BLUE_CORNER -> WinMethod.entries
    Winner.DRAW -> DrawMethod.entries
    Winner.NO_RESULT -> NoResultMethod.entries
    else -> emptyList()
}

@Preview(showBackground = true)
@Composable
private fun BoutInfoComponentPreview() {
    BoutScoringTheme {
        BoutInfoComponent(ParsedBout.example().info.copy()) { }
    }
}
