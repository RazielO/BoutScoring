package com.razielo.boutscoring.ui.components.boutscore

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.razielo.boutscoring.data.models.Bout
import com.razielo.boutscoring.data.models.DrawMethod
import com.razielo.boutscoring.data.models.NoResultMethod
import com.razielo.boutscoring.data.models.WinMethod
import com.razielo.boutscoring.data.models.Winner
import com.razielo.boutscoring.ui.components.common.TopBar
import kotlinx.coroutines.launch

@Composable
fun ScoreTopBar(
    snackbarHostState: SnackbarHostState,
    bout: Bout,
    goBackAction: () -> Unit,
    updateBoutResult: (Winner, WinMethod?, DrawMethod?, NoResultMethod?) -> Unit
) {
    val openAlertDialog = remember { mutableStateOf(false) }

    when {
        openAlertDialog.value -> {
            ResultDialog(snackbarHostState = snackbarHostState,
                bout = bout,
                onDismissRequest = { openAlertDialog.value = false },
                onConfirmation = { winner, method ->
                    openAlertDialog.value = false
                    var winMethod: WinMethod? = null
                    var drawMethod: DrawMethod? = null
                    var noResultMethod: NoResultMethod? = null

                    when (winner) {
                        Winner.BLUE_CORNER, Winner.RED_CORNER -> winMethod = method as WinMethod?
                        Winner.DRAW                           -> drawMethod = method as DrawMethod?
                        Winner.NO_RESULT                      -> noResultMethod =
                            method as NoResultMethod?
                    }

                    updateBoutResult(winner, winMethod, drawMethod, noResultMethod)
                })
        }
    }

    TopBar(titleText = "Score bout",
        goBack = true,
        onBack = goBackAction,
        actions = { ResultAction { openAlertDialog.value = true } })
}

@Composable
private fun ResultAction(onClick: () -> Unit) {
    TextButton(onClick) {
        Text("RESULT")
    }
}

private fun updateMethodSelected(bout: Bout): String? = when (bout.winner) {
    Winner.BLUE_CORNER, Winner.RED_CORNER -> bout.winMethod?.displayName
    Winner.DRAW                           -> bout.drawMethod?.displayName
    Winner.NO_RESULT                      -> bout.noResultMethod?.displayName
    null                                  -> null
}

@Composable
private fun ResultDialog(
    snackbarHostState: SnackbarHostState,
    bout: Bout,
    onDismissRequest: () -> Unit,
    onConfirmation: (Winner, Any?) -> Unit
) {
    val scope = rememberCoroutineScope()
    val resultOptions = Winner.entries.map { it.display }
    var methodOptions: List<String>? by remember { mutableStateOf(null) }
    var winner: Winner? by remember { mutableStateOf(bout.winner) }
    var winMethod: WinMethod? = bout.winMethod
    var drawMethod: DrawMethod? = bout.drawMethod
    var noResultMethod: NoResultMethod? = bout.noResultMethod
    var methodSelected: String? by remember { mutableStateOf(updateMethodSelected(bout)) }

    fun onResultSelection(selected: String) {
        winner = Winner.from(selected)
        if (winner == null) {
            return
        }
    }

    when {
        winner != null -> methodOptions = when (winner!!) {
            Winner.RED_CORNER, Winner.BLUE_CORNER -> WinMethod.entries.map { it.displayName }
            Winner.DRAW                           -> DrawMethod.entries.map { it.displayName }
            Winner.NO_RESULT                      -> NoResultMethod.entries.map { it.displayName }
        }
    }


    fun onMethodSelection(selected: String) {
        if (winner != null) {
            when (winner!!) {
                Winner.BLUE_CORNER, Winner.RED_CORNER -> winMethod = WinMethod.from(selected)
                Winner.DRAW                           -> drawMethod = DrawMethod.from(selected)
                Winner.NO_RESULT                      -> noResultMethod =
                    NoResultMethod.from(selected)
            }
        }
        methodSelected = updateMethodSelected(bout)
    }

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(375.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                HeadText("Set bout result", Modifier)

                Selection(
                    title = "Winner", options = resultOptions, selected = winner?.display
                ) { selected -> onResultSelection(selected) }

                if (methodOptions != null) {
                    Selection(
                        title = "Method", options = methodOptions!!, selected = methodSelected
                    ) { selected -> onMethodSelection(selected) }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    DialogActionButton("Dismiss") { onDismissRequest() }
                    DialogActionButton("Confirm") {
                        if (winner != null) {
                            val winnerValue = winner!!
                            val method: Any? = when (winnerValue) {
                                Winner.BLUE_CORNER, Winner.RED_CORNER -> winMethod
                                Winner.DRAW                           -> drawMethod
                                Winner.NO_RESULT                      -> noResultMethod
                            }
                            onConfirmation(winnerValue, method)
                        } else {
                            scope.launch {
                                snackbarHostState.showSnackbar("Select at least the winner")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DialogActionButton(text: String, onClick: () -> Unit) {
    TextButton(
        onClick,
        modifier = Modifier.padding(8.dp),
    ) {
        Text(text)
    }
}

@Composable
private fun Selection(
    title: String,
    options: List<String>,
    selected: String?,
    onSelectionChanged: (selection: String) -> Unit
) {
    Column {
        Text(
            text = title,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        DropdownSelection(
            list = options,
            preselected = selected ?: "Select $title",
            onSelectionChanged = onSelectionChanged,
            modifier = Modifier
        )
    }
}

@Composable
private fun DropdownSelection(
    list: List<String>,
    preselected: String,
    onSelectionChanged: (selection: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selected by remember { mutableStateOf(preselected) }
    var expanded by remember { mutableStateOf(false) } // initial value

    OutlinedCard(modifier = modifier
        .clickable { expanded = !expanded }
        .padding(horizontal = 16.dp)) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
        ) {
            Text(
                text = selected,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
            Icon(Icons.Outlined.ArrowDropDown, null, modifier = Modifier.padding(8.dp))

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                list.forEach { listEntry ->
                    DropdownSelectionItem(
                        text = listEntry,
                    ) {
                        selected = listEntry
                        expanded = false
                        onSelectionChanged(selected)
                    }
                }
            }
        }
    }
}

@Composable
private fun DropdownSelectionItem(text: String, onClick: () -> Unit) {
    DropdownMenuItem(
        onClick = onClick,
        text = { Text(text, modifier = Modifier.fillMaxWidth()) },
    )
}