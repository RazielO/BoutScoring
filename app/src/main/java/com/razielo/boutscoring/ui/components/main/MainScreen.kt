package com.razielo.boutscoring.ui.components.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.LifecycleOwner
import com.razielo.boutscoring.R
import com.razielo.boutscoring.data.BoutViewModel
import com.razielo.boutscoring.data.models.Fighter
import com.razielo.boutscoring.ui.components.common.TopBar
import com.razielo.boutscoring.ui.models.ParsedBout

@Composable
fun MainScreen(
    boutViewModel: BoutViewModel,
    owner: LifecycleOwner,
    goToBout: () -> Unit,
    goToAddBout: () -> Unit
) {
    var bouts by remember { mutableStateOf(emptyList<ParsedBout>()) }
    boutViewModel.bouts.observe(owner) { list -> list.let { bouts = it } }

    var filtered by remember { mutableStateOf(emptyList<ParsedBout>()) }
    boutViewModel.filtered.observe(owner) { list -> list.let { filtered = it } }

    var filtering by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }

    var searching by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }

    val goToBoutF: (Int) -> Unit = { index: Int ->
        boutViewModel.bout.value = if (!filtering) bouts[index] else filtered[index]
        goToBout()
    }

    val deleteBout: (ParsedBout) -> Unit = { boutViewModel.delete(it) }

    val filterBouts: (Fighter) -> Unit = {
        name = it.fullName
        filtering = true
        boutViewModel.getAllFighterBouts(it.fullName)
    }

    val title: String = if (searching) ""
    else if (!filtering && bouts.isEmpty()) stringResource(R.string.my_bouts_title)
    else if (!filtering) stringResource(R.string.my_bouts_count_title, bouts.size)
    else stringResource(R.string.filtered_bouts_title, name)

    Scaffold(topBar = {
        TopBar(titleText = title, goBack = filtering, onBack = {
            filtering = false
        }, actions = {
            MainComponentTopBarAction(searching, searchText, {
                searchText = it
                boutViewModel.searchAllFighterBouts(it)
            }, {
                searching = true
                filtering = true
            }, {
                searching = false
                searchText = ""
                filtering = false
            })
        })
    }, floatingActionButton = {
        FloatingButton(onClick = goToAddBout)
    }) { padding ->
        Surface(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            MainComponent(if (!filtering) bouts else filtered, goToBoutF, deleteBout, filterBouts)
        }
    }
}

@Composable
private fun FloatingButton(onClick: () -> Unit) {
    FloatingActionButton(onClick = onClick) {
        Icon(Icons.Filled.Add, "Add new bout.")
    }
}