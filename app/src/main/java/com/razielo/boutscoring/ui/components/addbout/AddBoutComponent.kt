package com.razielo.boutscoring.ui.components.addbout

import android.content.Context
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
import com.razielo.boutscoring.ui.theme.BoutScoringTheme

@Composable
fun AddBoutComponent(context: Context, topBarOnCLick: () -> Unit) {
    BoutScoringTheme {
        ScaffoldComponent(context, topBarOnCLick)
    }
}

@Composable
private fun ScaffoldComponent(context: Context, topBarOnCLick: () -> Unit) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(topBar = { AddBoutTopBar(topBarOnCLick) }, snackbarHost = {
        SnackbarHost(hostState = snackbarHostState)
    }, content = {
        Surface(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            AddBoutContent(snackbarHostState, context, topBarOnCLick)
        }
    })
}
