package com.razielo.boutscoring

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.razielo.boutscoring.data.BoutViewModel
import com.razielo.boutscoring.data.BoutViewModelFactory
import com.razielo.boutscoring.data.models.Bout
import com.razielo.boutscoring.data.models.Screen
import com.razielo.boutscoring.ui.components.addbout.AddBoutComponent
import com.razielo.boutscoring.ui.components.boutscore.BoutScoreComponent
import com.razielo.boutscoring.ui.components.common.TopBar
import com.razielo.boutscoring.ui.components.main.MainComponent
import com.razielo.boutscoring.ui.theme.BoutScoringTheme
import kotlinx.coroutines.Job

class MainActivity : ComponentActivity() {
    private val boutViewModel: BoutViewModel by viewModels {
        BoutViewModelFactory((application as BoutApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BoutScoringTheme {
                MainActivityComposable(
                    boutViewModel.bouts,
                    this,
                    { boutViewModel.insert(it) },
                    { boutViewModel.update(it) },
                    { boutViewModel.delete(it) }
                )
            }
        }
    }
}

@Composable
private fun MainActivityComposable(
    boutList: LiveData<List<Bout>>,
    owner: LifecycleOwner,
    insert: (Bout) -> Job,
    update: (Bout) -> Job, deleteBout: (String) -> Unit
) {
    var bouts by remember { mutableStateOf(emptyList<Bout>()) }
    boutList.observe(owner) { list -> list.let { bouts = it } }
    var bout: Bout? by remember { mutableStateOf(null) }

    var currentScreen by remember { mutableStateOf(Screen.MAIN) }

    val updateAndGoToMain: (Bout) -> Unit = {
        update(it)
        currentScreen = Screen.MAIN
    }
    val goToMain = {
        currentScreen = Screen.MAIN
    }
    val addAndGoToScoreBout: (Bout) -> Unit = {
        bout = it
        insert(it)
        currentScreen = Screen.SCORE_BOUT
    }
    val goToAddBout = { currentScreen = Screen.ADD_BOUT }
    val goToBout: (Int) -> Unit = {
        bout = bouts[it]
        currentScreen = Screen.SCORE_BOUT
    }
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(snackbarHost = {
        SnackbarHost(hostState = snackbarHostState)
    }, topBar = {
        MainTopBar(
            hidden = currentScreen == Screen.SCORE_BOUT,
            currentScreen = currentScreen,
            boutCount = bouts.size,
            goToMain = goToMain
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
                Screen.MAIN -> {
                    MainComponent(bouts, goToBout, deleteBout)
                }

                Screen.ADD_BOUT -> {
                    AddBoutComponent(
                        snackbarHostState = snackbarHostState, goToBoutScore = addAndGoToScoreBout
                    )
                }

                Screen.SCORE_BOUT -> {
                    if (bout != null) {
                        BoutScoreComponent(bout = bout!!, topBarOnCLick = updateAndGoToMain)
                    }
                }

                Screen.FILTERED_BOUTS -> {}
            }
        }
    }
}

@Composable
private fun MainTopBar(
    hidden: Boolean, currentScreen: Screen, boutCount: Int, goToMain: () -> Unit
) {
    if (!hidden) {
        TopBar(titleText = topBarTitle(currentScreen, boutCount),
            goBack = currentScreen != Screen.MAIN,
            onBack = goToMain,
            actions = {})
    }
}

@Composable
private fun FloatingButton(onClick: () -> Unit) {
    FloatingActionButton(onClick = onClick) {
        Icon(Icons.Filled.Add, "Add new bout.")
    }
}
