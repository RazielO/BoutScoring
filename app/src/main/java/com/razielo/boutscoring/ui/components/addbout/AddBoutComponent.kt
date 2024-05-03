package com.razielo.boutscoring.ui.components.addbout

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.razielo.boutscoring.data.models.Bout
import com.razielo.boutscoring.ui.components.common.TopBar

@Composable
fun AddBoutComponent(goBackToMain: () -> Unit, goToBoutScore: (Bout) -> Unit) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = { TopBar(titleText = "Add new bout", goBack = true, goBackToMain) },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        content = {
            Surface(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                AddBoutContent(snackbarHostState, goToBoutScore)
            }
        })
}
