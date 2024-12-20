package com.razielo.boutscoring.ui.components.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.razielo.boutscoring.R
import com.razielo.boutscoring.data.models.Fighter
import com.razielo.boutscoring.scoreColors
import com.razielo.boutscoring.ui.components.boutscore.HeadText
import com.razielo.boutscoring.ui.components.common.BoutScoreResult
import com.razielo.boutscoring.ui.models.ParsedBout

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
            ConfirmDeleteDialog(bout,
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
            .combinedClickable(onClick = { goToBout(index) }, onLongClick = {
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
            Column(Modifier.weight(5f)) {
                CardText(bout.redCorner.fullName,
                    Modifier.clickable { filterBouts(bout.redCorner) })
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CardText(stringResource(R.string.vs), Modifier)
                    if (bout.info.championship) Image(
                        painterResource(R.drawable.belt),
                        contentDescription = stringResource(R.string.championship_bout),
                        modifier = Modifier.height(28.dp)
                    )
                }
                CardText(bout.blueCorner.fullName,
                    Modifier.clickable { filterBouts(bout.blueCorner) })
            }
            Column(
                Modifier.weight(2f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(if (bout.info.winner != null) 0.dp else 8.dp)
            ) {
                CardText(redScore.toString(), Modifier, colors.first)
                BoutScoreResult(bout.info, Modifier)
                CardText(blueScore.toString(), Modifier, colors.second)
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
                    stringResource(
                        R.string.confirm_bout_deletion,
                        bout.redCorner.fullName,
                        bout.blueCorner.fullName
                    ), Modifier
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    DialogActionButton(stringResource(R.string.cancel)) { onDismissRequest() }
                    DialogActionButton(stringResource(R.string.confirm)) { onConfirmation() }
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
