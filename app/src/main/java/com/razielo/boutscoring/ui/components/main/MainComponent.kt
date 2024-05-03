package com.razielo.boutscoring.ui.components.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.razielo.boutscoring.data.models.Bout
import com.razielo.boutscoring.ui.components.common.TopBar

@Composable
fun MainComponent(
    bouts: List<Bout>, goToAddBout: () -> Unit, goToBout: (Int) -> Unit
) {
    val topBarText = if (bouts.isEmpty()) "My bouts" else "My ${bouts.size} bouts"

    Scaffold(topBar = {
        TopBar(titleText = topBarText, goBack = false)
    }, floatingActionButton = { FloatingButton(goToAddBout) }, content = {
        Surface(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Content(bouts, goToBout)
        }
    })
}

@Composable
fun Content(bouts: List<Bout>, goToBout: (Int) -> Unit) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(16.dp)
    ) {
        items(bouts.size) { index ->
            BoutListCard(index, bouts[index], goToBout)
        }
    }
}


@Composable
fun FloatingButton(onClick: () -> Unit) {
    FloatingActionButton(onClick = onClick) {
        Icon(Icons.Filled.Add, "Add new bout.")
    }
}
