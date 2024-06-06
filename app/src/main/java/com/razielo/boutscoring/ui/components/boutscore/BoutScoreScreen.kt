package com.razielo.boutscoring.ui.components.boutscore

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.razielo.boutscoring.R
import com.razielo.boutscoring.data.BoutViewModel
import com.razielo.boutscoring.ui.components.common.TopBar

@Composable
fun BoutScoreScreen(boutViewModel: BoutViewModel, goBack: () -> Unit, goToInfo: () -> Unit) {
    val snackbarHostState = remember { SnackbarHostState() }
    val bout = boutViewModel.bout.value

    if (bout == null) {
        goBack()
    }

    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }, topBar = {
        TopBar(
            titleText = stringResource(R.string.score_bout_title), goBack = true, onBack = goBack,
            actions = {
                IconButton(onClick = goToInfo) {
                    Icon(
                        Icons.Outlined.Info,
                        contentDescription = stringResource(R.string.bout_info_title)
                    )
                }
            }
        )
    }) { padding ->
        Surface(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            if (bout != null) {
                BoutScoreComponent(
                    snackbarHostState,
                    bout
                ) {
                    boutViewModel.update(it)
                    boutViewModel.bout.value = it
                }
            }
        }
    }
}
