package com.razielo.boutscoring.ui.components.addbout

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.razielo.boutscoring.R
import com.razielo.boutscoring.data.BoutViewModel
import com.razielo.boutscoring.ui.components.common.TopBar
import com.razielo.boutscoring.ui.models.ParsedBout
import com.razielo.boutscoring.ui.theme.BoutScoringTheme

@Composable
fun AddBoutScreen(boutViewModel: BoutViewModel, goToScore: () -> Unit, goBack: () -> Unit) {
    AddBoutScreenContent(
        insertBout = {
            boutViewModel.insert(it)
            boutViewModel.bout.value = it
            goToScore()
        },
        goBack = goBack
    )
}


@Composable
private fun AddBoutScreenContent(
    insertBout: (ParsedBout) -> Unit,
    goBack: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()

    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }, topBar = {
        TopBar(
            titleText = stringResource(R.string.add_new_bout_title), goBack = goBack
        )
    }) { padding ->
        Surface(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(scrollState),
            color = MaterialTheme.colorScheme.background
        ) {
            AddBoutComponent(snackbarHostState, insertBout)
        }
    }
}

@Preview
@Composable
private fun AddBoutScreenPreview() {
    BoutScoringTheme {
        AddBoutScreenContent({}) {}
    }
}