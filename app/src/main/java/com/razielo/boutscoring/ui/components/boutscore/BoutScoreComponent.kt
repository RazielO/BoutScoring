package com.razielo.boutscoring.ui.components.boutscore

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.razielo.boutscoring.data.models.Bout

@Composable
fun BoutScoreComponent(bout: Bout, topBarOnCLick: (Bout) -> Unit) {
    val snackbarHostState = remember { SnackbarHostState() }
    val boutState = remember { mutableStateOf(bout) }

    Scaffold(topBar = {
        ScoreTopBar(
            snackbarHostState,
            boutState.value,
            { topBarOnCLick(boutState.value) }
        ) { winner, winMethod, drawMethod, noResultMethod ->
            boutState.value = bout.copy(
                winner = winner,
                winMethod = winMethod,
                drawMethod = drawMethod,
                noResultMethod = noResultMethod
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
            Content(snackbarHostState, boutState.value) { round, values ->
                boutState.value =
                    boutState.value.copy(scores = boutState.value.scores.toMutableMap().apply {
                        set(round, values)
                    })
            }
        }
    })
}