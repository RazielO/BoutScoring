package com.razielo.boutscoring.ui.components.boutscore

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.razielo.boutscoring.data.models.ParsedBout

@Composable
fun BoutScoreComponent(bout: ParsedBout, topBarOnCLick: (ParsedBout) -> Unit) {
    val snackbarHostState = remember { SnackbarHostState() }
    var boutState by remember { mutableStateOf(bout) }

    Scaffold(topBar = {
        ScoreTopBar(
            snackbarHostState,
            boutState.info,
            { topBarOnCLick(boutState) }) { winner, winMethod, drawMethod, noResultMethod ->
            boutState = boutState.copy(
                info = boutState.info.copy(
                    winner = winner,
                    winMethod = winMethod,
                    drawMethod = drawMethod,
                    noResultMethod = noResultMethod
                )
            )
        }
    }, snackbarHost = {
        SnackbarHost(hostState = snackbarHostState)
    }, content = {
        Surface(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Content(snackbarHostState, boutState) { round, values ->
                val newScores = boutState.bout.scores.toMutableMap().apply { set(round, values) }
                boutState = boutState.copy(bout = boutState.bout.copy(scores = newScores))
            }
        }
    })
}