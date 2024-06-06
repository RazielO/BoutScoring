package com.razielo.boutscoring

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.razielo.boutscoring.data.BoutViewModel
import com.razielo.boutscoring.data.BoutViewModelFactory
import com.razielo.boutscoring.ui.components.addbout.AddBoutScreen
import com.razielo.boutscoring.ui.components.boutinfo.BoutInfoScreen
import com.razielo.boutscoring.ui.components.boutscore.BoutScoreScreen
import com.razielo.boutscoring.ui.components.main.MainScreen
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
    val navController = rememberNavController()

    NavHost(navController = navController,
        startDestination = "main",
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }) {
        composable(route = "main") {
            MainScreen(boutViewModel = boutViewModel,
                owner = owner,
                goToBout = { navController.navigate("score") }) {
                navController.navigate("add_bout")
            }
        }
        composable(route = "score") {
            BoutScoreScreen(boutViewModel = boutViewModel,
                goBack = { navController.navigate("main") },
                goToInfo = { navController.navigate("info") })
        }
        composable(route = "info") {
            BoutInfoScreen(boutViewModel = boutViewModel) {
                navController.navigate("score") { popUpTo("score") }
            }
        }
        composable(route = "add_bout") {
            AddBoutScreen(boutViewModel = boutViewModel, goToScore = {
                navController.navigate("score") {
                    popUpTo("main")
                }
            }) {
                navController.navigate("main")
            }
        }
    }
}