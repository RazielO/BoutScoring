package com.razielo.boutscoring

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.razielo.boutscoring.data.models.Bout
import com.razielo.boutscoring.ui.components.addbout.AddBoutComponent
import com.razielo.boutscoring.ui.components.boutscore.BoutScoreComponent
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

private enum class Screen {
    MAIN,
    FILTERED_BOUTS,
    ADD_BOUT,
    SCORE_BOUT
}

@Composable
private fun MainActivityComposable(boutList: List<Bout>) {
    val bouts = boutList.toMutableList()
    var currentScreen by remember { mutableStateOf(Screen.MAIN) }
    var boutIndex = 0
    val goToMain = { currentScreen = Screen.MAIN }
    val addAndGoToScoreBout = { bout: Bout ->
        bouts.add(bout)
        boutIndex = bouts.lastIndex
        currentScreen = Screen.SCORE_BOUT
    }
    val goToAddBout = { currentScreen = Screen.ADD_BOUT }
    val goToBout = { index: Int ->
        boutIndex = index
        currentScreen = Screen.SCORE_BOUT
    }

    when (currentScreen) {
        Screen.MAIN -> {
            MainComponent(
                bouts = bouts,
                goToAddBout = goToAddBout,
                goToBout = goToBout
            )
        }

        Screen.ADD_BOUT -> {
            AddBoutComponent(
                goBackToMain = goToMain, goToBoutScore = addAndGoToScoreBout
            )
        }

        Screen.SCORE_BOUT -> {
            val bout = bouts[boutIndex]
            BoutScoreComponent(bout = bout, topBarOnCLick = goToMain)
        }

        Screen.FILTERED_BOUTS -> {}
    }
}
