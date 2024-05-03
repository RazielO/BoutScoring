package com.razielo.boutscoring.ui.components.main

import android.content.Context
import android.content.Intent
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
import androidx.core.content.ContextCompat
import com.razielo.boutscoring.AddBoutActivity
import com.razielo.boutscoring.data.models.Bout
import com.razielo.boutscoring.ui.components.common.TopBar
import com.razielo.boutscoring.ui.theme.BoutScoringTheme

@Composable
fun MainComponent(context: Context, bouts: List<Bout>) {
    BoutScoringTheme {
        MainScaffold(context, bouts)
    }
}

@Composable
private fun MainScaffold(context: Context, bouts: List<Bout>) {
    val topBarText = if (bouts.isEmpty()) "My bouts" else "My ${bouts.size} bouts"
    Scaffold(topBar = {
        TopBar(titleText = topBarText, goBack = false)
    }, floatingActionButton = { FloatingButton(context) }, content = {
        Surface(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Content(context, bouts)
        }
    })
}

@Composable
fun Content(context: Context, bouts: List<Bout>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(16.dp)
    ) {
        items(bouts.size) { index ->
            BoutListCard(context, bouts[index])
        }
    }
}


@Composable
fun FloatingButton(context: Context) {
    FloatingActionButton(onClick = {
        val intent = Intent(context, AddBoutActivity::class.java)
        ContextCompat.startActivity(context, intent, null)
    }) {
        Icon(Icons.Filled.Add, "Add new bout.")
    }
}
