package com.razielo.boutscoring

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import com.razielo.boutscoring.data.BoutViewModel
import com.razielo.boutscoring.data.BoutViewModelFactory
import com.razielo.boutscoring.data.models.Bout
import com.razielo.boutscoring.data.models.Fighter
import com.razielo.boutscoring.data.models.ParsedBout
import com.razielo.boutscoring.data.models.Screen
import com.razielo.boutscoring.ui.components.addbout.AddBoutComponent
import com.razielo.boutscoring.ui.components.boutscore.BoutScoreComponent
import com.razielo.boutscoring.ui.components.common.TopBar
import com.razielo.boutscoring.ui.components.main.MainComponent
import com.razielo.boutscoring.ui.theme.BoutScoringTheme

class MainActivity : ComponentActivity() {
    private val boutViewModel: BoutViewModel by viewModels {
        BoutViewModelFactory((application as BoutApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BoutScoringTheme {
                MainActivityComposable(
                    boutViewModel, this
                )
            }
        }
    }
}

@Composable
private fun MainActivityComposable(boutViewModel: BoutViewModel, owner: LifecycleOwner) {
    var bouts by remember { mutableStateOf(emptyList<ParsedBout>()) }
    boutViewModel.bouts.observe(owner) { list ->
        list.let {
            bouts = it.mapNotNull { b -> ParsedBout.fromBoutWithFighters(b) }
        }
    }

    var bout: ParsedBout? by remember { mutableStateOf(null) }
    boutViewModel.bout.observe(owner) { value -> value.let { bout = it } }

    var filtered by remember { mutableStateOf(emptyList<ParsedBout>()) }
    boutViewModel.filtered.observe(owner) { list -> list.let { filtered = it } }

    var searching by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }

    var name by remember { mutableStateOf("") }
    var currentScreen by remember { mutableStateOf(Screen.MAIN) }

    when {
        bout != null -> currentScreen = Screen.SCORE_BOUT
        filtered.isNotEmpty() -> currentScreen = Screen.FILTERED_BOUTS
    }

    val reset = {
        bout = null
        filtered = emptyList()
        searching = false
        searchText = ""
    }

    val updateAndGoToMain: (Bout) -> Unit = {
        boutViewModel.update(it)
        currentScreen = Screen.MAIN
        reset()
    }
    val goToMain = {
        currentScreen = Screen.MAIN
        reset()
    }
    val addAndGoToScoreBout: (ParsedBout) -> Unit = {
        bout = it
        boutViewModel.insert(it)
        currentScreen = Screen.SCORE_BOUT
    }
    val goToAddBout = { currentScreen = Screen.ADD_BOUT }
    val goToBout: (Int) -> Unit = {
        if (currentScreen == Screen.MAIN) {
            boutViewModel.getBoutById(bouts[it].bout.id)
        } else {
            boutViewModel.getBoutById(filtered[it].bout.id)
        }
    }
    val deleteBout: (ParsedBout) -> Unit = { boutViewModel.delete(it) }
    val filterBouts: (Fighter) -> Unit = {
        boutViewModel.getAllFighterBouts(it.fullName)
        name = it.fullName
        searching = false
        searchText = ""
    }
    val onSearchTextChange: (String) -> Unit = {
        searchText = it
        boutViewModel.searchAllFighterBouts(searchText)
    }
    val snackbarHostState = remember { SnackbarHostState() }


    Scaffold(snackbarHost = {
        SnackbarHost(hostState = snackbarHostState)
    }, topBar = {
        MainTopBar(
            hidden = currentScreen == Screen.SCORE_BOUT,
            currentScreen = currentScreen,
            boutCount = bouts.size,
            goToMain = goToMain,
            name = name,
            searching = searching,
            searchText = searchText,
            onSearchTextChange = onSearchTextChange,
            onSearchClick = {
                reset()
                searching = true
            },
            onCancelClick = goToMain
        )
    }, floatingActionButton = {
        if (currentScreen == Screen.MAIN) {
            FloatingButton(goToAddBout)
        }
    }) {
        Surface(
            modifier = androidx.compose.ui.Modifier
                .padding(it)
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            when (currentScreen) {
                Screen.MAIN -> MainComponent(bouts, goToBout, deleteBout, filterBouts)
                Screen.FILTERED_BOUTS -> MainComponent(filtered, goToBout, deleteBout, filterBouts)
                Screen.ADD_BOUT -> AddBoutComponent(snackbarHostState, addAndGoToScoreBout)
                Screen.SCORE_BOUT -> {
                    if (bout != null) {
                        BoutScoreComponent(bout!!, updateAndGoToMain)
                    }
                }
            }
        }
    }
}

@Composable
private fun MainTopBar(
    hidden: Boolean,
    currentScreen: Screen,
    boutCount: Int,
    goToMain: () -> Unit,
    name: String,
    searching: Boolean,
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onSearchClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    if (!hidden) {
        TopBar(titleText = if (searching) "" else topBarTitle(currentScreen, boutCount, name),
            goBack = currentScreen != Screen.MAIN,
            onBack = goToMain,
            actions = {
                if (searching) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = searchText,
                            onValueChange = onSearchTextChange,
                            placeholder = { Text("Search") },
                            singleLine = true
                        )
                        IconButton(onClick = onCancelClick) {
                            Icon(Icons.Default.Clear, contentDescription = "Cancel search")
                        }
                    }
                } else {
                    IconButton(onClick = onSearchClick) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                }
            })
    }
}

@Composable
private fun FloatingButton(onClick: () -> Unit) {
    FloatingActionButton(onClick = onClick) {
        Icon(Icons.Filled.Add, "Add new bout.")
    }
}
