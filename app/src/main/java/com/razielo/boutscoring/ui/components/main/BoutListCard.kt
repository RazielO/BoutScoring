package com.razielo.boutscoring.ui.components.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.razielo.boutscoring.data.models.Fighter
import com.razielo.boutscoring.data.models.ParsedBout
import com.razielo.boutscoring.scoreColors
import com.razielo.boutscoring.ui.components.boutscore.HeadText
import com.razielo.boutscoring.ui.components.common.BoutScoreResult

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BoutListCard(
    index: Int,
    bout: ParsedBout,
    goToBout: (Int) -> Unit,
    deleteBout: (ParsedBout) -> Unit,
    filterBouts: (Fighter) -> Unit
) {
    val haptics = LocalHapticFeedback.current
    var openAlertDialog by remember { mutableStateOf(false) }

    when {
        openAlertDialog -> {
            ConfirmDeleteDialog(
                bout,
                onDismissRequest = { openAlertDialog = false },
                onConfirmation = {
                    deleteBout(bout)
                    openAlertDialog = false
                })
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(onClick = {}, onLongClick = {
                haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                openAlertDialog = true
            })
    ) {
        val redScore = bout.bout.scores.values.sumOf { it.first }
        val blueScore = bout.bout.scores.values.sumOf { it.second }
        val colors = scoreColors(
            Pair(redScore, blueScore), MaterialTheme.colorScheme.onSurfaceVariant
        )

        Row(
            modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(7f)) {
                Row {
                    CardText(
                        bout.redCorner.fullName,
                        Modifier
                            .weight(5f)
                            .clickable { filterBouts(bout.redCorner) })
                    CardText(redScore.toString(), Modifier.weight(1f), colors.first)
                }
                Row {
                    CardText("vs", Modifier.weight(3f))
                    BoutScoreResult(bout.bout, Modifier.weight(1f))
                }
                Row {
                    CardText(
                        bout.blueCorner.fullName,
                        Modifier
                            .weight(5f)
                            .clickable { filterBouts(bout.blueCorner) })
                    CardText(blueScore.toString(), Modifier.weight(1f), colors.second)
                }
            }
            Box(modifier = Modifier.weight(1f)) {
                IconButton(onClick = {
                    goToBout(index)
                }) {
                    Icon(Icons.Outlined.KeyboardArrowRight, "Go to bout score")
                }
            }
        }
    }
}

@Composable
private fun CardText(
    text: String, modifier: Modifier, color: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {
    Text(
        text,
        fontSize = MaterialTheme.typography.bodyLarge.fontSize * 1.2f,
        modifier = modifier,
        color = color
    )
}

@Composable
private fun ConfirmDeleteDialog(
    bout: ParsedBout, onDismissRequest: () -> Unit, onConfirmation: () -> Unit
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                HeadText(
                    "Are you sure you want to delete ${bout.redCorner.fullName} vs ${bout.blueCorner.fullName}?",
                    Modifier
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    DialogActionButton("Dismiss") { onDismissRequest() }
                    DialogActionButton("Confirm") { onConfirmation() }
                }
            }
        }
    }
}

@Composable
private fun DialogActionButton(text: String, onClick: () -> Unit) {
    TextButton(
        onClick,
    ) {
        Text(text)
    }
}
