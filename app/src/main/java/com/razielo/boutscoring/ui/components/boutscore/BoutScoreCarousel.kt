package com.razielo.boutscoring.ui.components.boutscore

import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.razielo.boutscoring.R
import com.razielo.boutscoring.data.models.Bout
import com.razielo.boutscoring.ui.components.common.CardStyleTextField
import com.razielo.boutscoring.ui.models.ParsedBout
import com.razielo.boutscoring.ui.theme.BoutScoringTheme
import com.razielo.boutscoring.ui.theme.blueContainerDark
import com.razielo.boutscoring.ui.theme.redContainerDark
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Composable
fun BoutScoreCarousel(
    boutParam: ParsedBout,
    showErrorSnackbar: () -> Job,
    update: (ParsedBout) -> Unit,
) {
    var bout by remember { mutableStateOf(boutParam) }
    val pagerState = rememberPagerState(pageCount = { bout.bout.rounds }, initialPage = 0)

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                if (pagerState.currentPage > 0) {
                    RoundHint(pagerState.currentPage) { pagerState.requestScrollToPage(it) }
                }
            }
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(8f)
            ) { i ->
                PagerCard(
                    i,
                    Modifier.weight(8f),
                    bout.bout.scores[i + 1],
                    updateScore =
                        { i, values ->
                            bout = bout.copy(bout = updateRound(bout.bout, i + 1, values))
                            update(bout)
                        },
                    addQuickNote = { note ->
                        val notes = bout.info.notes
                        val newNotes = if (note.isEmpty()) {
                            notes
                        } else if (notes.isEmpty()) {
                            note.trim()
                        } else {
                            "$notes\n${note.trim()}"
                        }
                        bout = bout.copy(info = bout.info.copy(notes = newNotes))
                        Log.d("$", "Notes: \"$newNotes\"")
                        update(bout)
                    }
                )
            }
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                if (pagerState.currentPage < bout.bout.rounds - 1) {
                    RoundHint(pagerState.currentPage + 2) { pagerState.requestScrollToPage(it) }
                }
            }
        }

        DotsIndicator(
            pagerState = pagerState,
            activeColor = MaterialTheme.colorScheme.primary,
            inactiveColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
        )
    }
}

private fun updateRound(bout: Bout, round: Int, values: Pair<Int, Int>): Bout =
    bout.copy(scores = bout.scores.toMutableMap().apply { set(round, values) })

@Composable
private fun PagerCard(
    round: Int,
    modifier: Modifier,
    score: Pair<Int, Int>?,
    updateScore: (Int, Pair<Int, Int>) -> Unit,
    addQuickNote: (String) -> Unit
) {
    var showQuickNoteDialog by remember { mutableStateOf(false) }

    Card(
        modifier = modifier,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Text(
                stringResource(R.string.round).uppercase(),
                style = MaterialTheme.typography.titleMedium,
                color = Color.Gray,
                fontWeight = FontWeight.Bold,
            )

            Text(
                (round + 1).toString(),
                style = MaterialTheme.typography.displayLarge,
                fontWeight = FontWeight.W900,
            )


            if (score != null && score.first != 0 && score.second != 0) {
                val cardColor =
                    if (score.first > score.second) redContainerDark else blueContainerDark
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = cardColor.copy(alpha = 0.5f)
                    )
                ) {
                    // Parsed to int because for some reason, scores sometimes appear as 10.0-9.0
                    Text(
                        text = "${score.first.toInt()}-${score.second.toInt()}",
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                Spacer(
                    Modifier
                        .padding(horizontal = 24.dp, vertical = 12.dp)
                        .height(MaterialTheme.typography.titleLarge.fontSize.value.dp * 1.2f)
                )
            }

            Spacer(Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ScoringButton(
                    text = "10-9",
                    modifier = Modifier.weight(1f),
                    color = redContainerDark,
                ) { updateScore(round, Pair(10, 9)) }
                ScoringButton(
                    text = "9-10",
                    color = blueContainerDark,
                    modifier = Modifier.weight(1f),
                ) { updateScore(round, Pair(9, 10)) }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ScoringButton(
                    text = "10-8",
                    modifier = Modifier.weight(1f),
                    color = redContainerDark,
                ) { updateScore(round, Pair(10, 8)) }
                ScoringButton(
                    text = "8-10",
                    modifier = Modifier.weight(1f),
                    color = blueContainerDark,
                ) { updateScore(round, Pair(8, 10)) }
            }

            Spacer(Modifier.height(8.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                ),
                onClick = { showQuickNoteDialog = true }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = null,
                        modifier = Modifier.size(MaterialTheme.typography.bodySmall.fontSize.value.dp)
                    )
                    Text(
                        stringResource(R.string.add_quick_note),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }

    if (showQuickNoteDialog) {
        QuickNoteDialog(
            round + 1,
            onDismiss = { showQuickNoteDialog = false },
            onConfirm = {
                addQuickNote(it)
                showQuickNoteDialog = false
            }
        )
    }
}

@Composable
private fun QuickNoteDialog(
    round: Int,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
) {
    var note by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Title
                Text(
                    stringResource(R.string.add_quick_note),
                    Modifier
                        .padding(bottom = 24.dp)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface
                )

                CardStyleTextField(
                    hintText = stringResource(R.string.add_quick_note),
                    value = note,
                    modifier = Modifier
                        .defaultMinSize(minHeight = 200.dp)
                        .fillMaxWidth()
                ) {
                    note = it
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier
                            .height(48.dp)
                            .weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                    ) {
                        Text(
                            stringResource(R.string.cancel),
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }

                    Button(
                        onClick = { onConfirm("Round $round: $note") },
                        modifier = Modifier
                            .height(48.dp)
                            .weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(stringResource(R.string.save))
                    }
                }
            }
        }
    }
}

@Composable
private fun ScoringButton(
    text: String,
    modifier: Modifier,
    color: Color,
    onClick: () -> Unit,
) {
    val style = MaterialTheme.typography.titleLarge.copy(
        fontWeight = FontWeight.Bold,
        color = color
    )

    OutlinedButton(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(width = 2.dp, color = color),
        contentPadding = PaddingValues(vertical = 12.dp)
    ) {
        Text(text, style = style)
    }
}

@Composable
private fun RoundHint(roundN: Int, updateRound: (Int) -> Unit) {
    val textStyle = MaterialTheme.typography.titleMedium.copy(
        color = Color.Gray,
        fontWeight = FontWeight.Bold
    )

    TextButton(
        onClick = { updateRound(roundN - 1) },
        contentPadding = PaddingValues(0.dp)

    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                stringResource(R.string.round_abbr).uppercase(),
                style = textStyle,
                maxLines = 1
            )
            Text(
                roundN.toString(),
                style = textStyle.copy(fontSize = MaterialTheme.typography.headlineMedium.fontSize),
            )
        }
    }
}

@Composable
private fun DotsIndicator(
    pagerState: PagerState,
    activeColor: Color,
    inactiveColor: Color,
    dotSize: Dp = 8.dp,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Create a dot for each page
        repeat(pagerState.pageCount) { page ->
            val isActive = page == pagerState.currentPage
            val color = if (isActive) activeColor else inactiveColor

            // Animate dot size when active
            val size by animateDpAsState(
                targetValue = if (isActive) dotSize * 1.5f else dotSize,
                animationSpec = tween(300)
            )

            Box(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .size(size)
                    .background(color, CircleShape)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun QuickNoteDialogPreview() {
    BoutScoringTheme {
        QuickNoteDialog(
            round = 1,
            onDismiss = {},
            onConfirm = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BoutScoreCarouselPreview() {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val errorMsg = stringResource(R.string.score_round_error)

    BoutScoringTheme {
        BoutScoreCarousel(
            ParsedBout.example(),
            {

                scope.launch {
                    snackbarHostState.showSnackbar(errorMsg)
                }
            }
        ) { }
    }
}