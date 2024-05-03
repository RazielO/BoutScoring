package com.razielo.boutscoring

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import com.razielo.boutscoring.data.models.Bout
import com.razielo.boutscoring.data.models.Screen
import com.razielo.boutscoring.ui.components.addbout.AddBoutComponent
import com.razielo.boutscoring.ui.components.boutscore.BoutScoreComponent
import com.razielo.boutscoring.ui.components.common.TopBar
import com.razielo.boutscoring.ui.components.main.MainComponent
import com.razielo.boutscoring.ui.theme.BoutScoringTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bouts = (1 .. 100).map { generateRandomBout() }

        setContent {
            BoutScoringTheme {
                MainActivityComposable(boutList = bouts)
            }
        }
    }
}

@Composable
private fun MainActivityComposable(boutList: List<Bout>) {
    var bouts by remember { mutableStateOf(boutList) }
    var currentScreen by remember { mutableStateOf(Screen.MAIN) }
    var boutIndex = 0
    val goToMain = { currentScreen = Screen.MAIN }
    val addAndGoToScoreBout = { bout: Bout ->
        bouts = bouts.toMutableList().apply { this.add(bout) }
        boutIndex = bouts.lastIndex
        currentScreen = Screen.SCORE_BOUT
    }
    val goToAddBout = { currentScreen = Screen.ADD_BOUT }
    val goToBout = { index: Int ->
        boutIndex = index
        currentScreen = Screen.SCORE_BOUT
    }
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            MainTopBar(
                hidden = currentScreen == Screen.SCORE_BOUT,
                currentScreen = currentScreen,
                boutCount = bouts.size,
                goToMain = goToMain
            )
        },
        floatingActionButton = {
            if (currentScreen == Screen.MAIN) {
                FloatingButton(goToAddBout)
            }
        },
        content = {
            Surface(
                modifier = androidx.compose.ui.Modifier
                    .padding(it)
                    .fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                when (currentScreen) {
                    Screen.MAIN -> {
                        MainComponent(
                            bouts = bouts, goToBout = goToBout
                        )
                    }

                    Screen.ADD_BOUT -> {
                        AddBoutComponent(
                            snackbarHostState = snackbarHostState,
                            goToBoutScore = addAndGoToScoreBout
                        )
                    }

                    Screen.SCORE_BOUT -> {
                        val bout = bouts[boutIndex]
                        BoutScoreComponent(bout = bout, topBarOnCLick = goToMain)
                    }

                    Screen.FILTERED_BOUTS -> {}
                }
            }
        })
}

@Composable
private fun MainTopBar(
    hidden: Boolean,
    currentScreen: Screen,
    boutCount: Int,
    goToMain: () -> Unit
) {
    if (!hidden) {
        TopBar(
            titleText = topBarTitle(currentScreen, boutCount),
            goBack = currentScreen != Screen.MAIN,
            onBack = goToMain,
            actions = {
            }
        )
    }
}

@Composable
private fun FloatingButton(onClick: () -> Unit) {
    FloatingActionButton(onClick = onClick) {
        Icon(Icons.Filled.Add, "Add new bout.")
    }
}
