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
import com.razielo.boutscoring.R
import com.razielo.boutscoring.data.BoutViewModel
import com.razielo.boutscoring.ui.components.common.TopBar

@Composable
fun AddBoutScreen(boutViewModel: BoutViewModel, goToScore: () -> Unit, goBack: () -> Unit) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()

    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }, topBar = {
        TopBar(
            titleText = stringResource(R.string.add_new_bout_title), goBack = true, onBack = goBack
        )
    }) { padding ->
        Surface(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(scrollState),
            color = MaterialTheme.colorScheme.background
        ) {
            AddBoutComponent(snackbarHostState) {
                boutViewModel.insert(it)
                boutViewModel.bout.value = it
                goToScore()
            }
        }
    }
}
