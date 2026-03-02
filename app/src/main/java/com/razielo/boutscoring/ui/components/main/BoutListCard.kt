package com.razielo.boutscoring.ui.components.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.AbsoluteCutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.razielo.boutscoring.R
import com.razielo.boutscoring.data.models.BoutInfo
import com.razielo.boutscoring.resultText
import com.razielo.boutscoring.ui.components.common.Pill
import com.razielo.boutscoring.ui.components.common.resultColor
import com.razielo.boutscoring.ui.models.ParsedBout
import com.razielo.boutscoring.ui.theme.BoutScoringTheme
import com.razielo.boutscoring.ui.theme.blueContainerDark
import com.razielo.boutscoring.ui.theme.redContainerDark


/**
 * Card element that shows an overview of a bout, it includes each corner's names, scores, a
 * graph of the bout, a button that shows/hides the notes and the result
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BoutListCard(
    index: Int,
    bout: ParsedBout,
    goToBout: (Int) -> Unit,
    deleteBout: (ParsedBout) -> Unit
) {
    val haptics = LocalHapticFeedback.current
    var openAlertDialog by remember { mutableStateOf(false) }
    var expandedNotes by remember { mutableStateOf(false) }

    when {
        openAlertDialog -> {
            ConfirmDeleteDialog(
                bout,
                onDismissRequest = { openAlertDialog = false },
                onConfirm = {
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
            }), elevation = CardDefaults.cardElevation(10.dp)
    ) {
        val redScore = bout.bout.scores.values.sumOf { it.first }
        val blueScore = bout.bout.scores.values.sumOf { it.second }

        Box {
            CornerAccent(
                color = redContainerDark, // red
                alignment = Alignment.TopStart, modifier = Modifier.align(Alignment.TopStart)
            )
            CornerAccent(
                color = blueContainerDark, // blue
                alignment = Alignment.TopEnd, modifier = Modifier.align(Alignment.TopEnd)
            )


            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth(),
                ) {
                    // Corners colors row
                    Row {
                        CornerHeader(
                            stringResource(R.string.red_corner),
                            redContainerDark,
                            Modifier.weight(3f)
                        )
                        if (bout.info.championship) {
                            Image(
                                painterResource(R.drawable.belt),
                                contentDescription = stringResource(R.string.championship_bout),
                                Modifier.height(20.dp)
                            )
                        } else {
                            Spacer(Modifier.weight(1f))
                        }
                        CornerHeader(
                            stringResource(R.string.blue_corner),
                            blueContainerDark,
                            Modifier.weight(3f),
                            TextAlign.End
                        )
                    }
                    // Corners names row
                    Row {
                        FighterName(
                            bout.redCorner.fullName, redContainerDark, Modifier.weight(3f)
                        )
                        Text(
                            stringResource(R.string.vs).uppercase(),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.ExtraBold,
                            fontStyle = FontStyle.Italic,
                            color = Color.Gray,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(1f)
                        )
                        FighterName(
                            bout.blueCorner.fullName,
                            blueContainerDark,
                            Modifier.weight(3f),
                            TextAlign.End
                        )
                    }
                    // Corners scores row
                    Row {
                        CornerScore(
                            redScore,
                            redContainerDark,
                            Modifier.weight(3f)
                        )
                        Spacer(Modifier.weight(1f))
                        CornerScore(
                            blueScore,
                            blueContainerDark,
                            Modifier.weight(3f),
                            TextAlign.End
                        )
                    }
                }

                // Graph
                Box {
                    // Display the graph only if any round was scored
                    if (bout.bout.scores.any { it.value.first != 0 && it.value.second != 0 }) {
                        CumulativeScoreGraph(
                            scores = bout.bout.scores,
                            modifier = Modifier.padding(top = 12.dp),
                        )
                    }
                }

                // Bottom
                BottomPills(bout.info, expandedNotes) {
                    expandedNotes = !expandedNotes
                }
            }
        }
    }
}

/**
 * Element to show each corner's color
 */
@Composable
private fun CornerHeader(
    label: String,
    color: Color,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start
) {
    Text(
        label.uppercase(),
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.labelMedium,
        color = color,
        textAlign = textAlign,
        modifier = modifier
    )
}

/**
 * Element to show each corner's fighter name
 */
@Composable
private fun FighterName(
    name: String,
    color: Color,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start
) {
    Text(
        name,
        fontWeight = FontWeight.W600,
        style = MaterialTheme.typography.headlineSmall,
        color = color,
        textAlign = textAlign,
        modifier = modifier
    )
}

/**
 * Element to show each corner's score
 */
@Composable
private fun CornerScore(
    score: Int,
    color: Color,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start
) {
    Text(
        score.toString(),
        fontWeight = FontWeight.W900,
        style = MaterialTheme.typography.headlineSmall,
        color = color,
        textAlign = textAlign,
        modifier = modifier
    )
}

/**
 * Bottom element of the card, two pills and the notes are shown when relevant:
 * - If the bout has notes, it will show a button to show them, if the button is clicked, it will show/hide the notes
 * - If the bout has a result, it will show a pill with the result
 */
@Composable
private fun BottomPills(info: BoutInfo, expandedNotes: Boolean, onClickNotes: () -> Unit) {
    val hasNotes = info.notes.isNotEmpty()
    val hasWinner = info.winner != null

    if (hasNotes || hasWinner) {
        HorizontalDivider(Modifier.padding(top = 6.dp))
        AnimatedVisibility(
            visible = expandedNotes,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(12.dp)
            ) {
                Text(
                    info.notes,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Box(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth(),
        ) {
            if (hasNotes) {
                val notesTextId = if (expandedNotes) R.string.hide_notes else R.string.show_notes
                Pill(
                    text = stringResource(notesTextId),
                    modifier = Modifier
                        .clickable(onClick = onClickNotes)
                        .align(Alignment.TopStart),
                    background = MaterialTheme.colorScheme.surfaceColorAtElevation(
                        100.dp
                    ),
                    textColor = MaterialTheme.colorScheme.onSurface
                )
            }

            if (hasWinner) {
                Pill(
                    text = info.resultText(),
                    modifier = Modifier.align(Alignment.TopEnd),
                    background = resultColor(info.winner),
                )
            }
        }

    }
}


/**
 * Element used to color a corner
 */
@Composable
private fun CornerAccent(
    color: Color, alignment: Alignment, modifier: Modifier = Modifier
) {
    val cornerSize = 30.dp
    val shape = when (alignment) {
        Alignment.TopStart -> AbsoluteCutCornerShape(bottomRight = cornerSize)
        Alignment.TopEnd -> AbsoluteCutCornerShape(bottomLeft = cornerSize)
        else -> AbsoluteCutCornerShape(0.dp)
    }

    Box(
        modifier
            .size(cornerSize)
            .clip(shape)
            .background(color)
    )
}

/**
 * Bout list card preview
 */
@Preview(showBackground = false)
@Composable
private fun BoutListCardPreview() {
    BoutScoringTheme {
        BoutListCard(
            index = 1,
            bout = ParsedBout.example(),
            goToBout = {},
            deleteBout = {}
        )
    }
}


