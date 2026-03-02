package com.razielo.boutscoring.ui.components.main

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.expandIn
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import com.razielo.boutscoring.R
import com.razielo.boutscoring.data.BoutViewModel
import com.razielo.boutscoring.data.models.Bout
import com.razielo.boutscoring.data.models.BoutInfo
import com.razielo.boutscoring.data.models.Fighter
import com.razielo.boutscoring.data.models.enums.DrawMethod
import com.razielo.boutscoring.data.models.enums.WeightClass
import com.razielo.boutscoring.data.models.enums.WinMethod
import com.razielo.boutscoring.data.models.enums.Winner
import com.razielo.boutscoring.ui.models.ParsedBout
import com.razielo.boutscoring.ui.theme.BoutScoringTheme
import java.util.Date

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

    var searching by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }

    val goToBoutScoring: (Int) -> Unit = { index: Int ->
        boutViewModel.bout.value = if (!searching) bouts[index] else filtered[index]
        goToBout()
    }
    val deleteBout: (ParsedBout) -> Unit = { boutViewModel.delete(it) }

    val onSearchTextChange: (String) -> Unit = {
        searchText = it
        boutViewModel.searchAllFighterBouts(it)
    }

    val toggleSearch = {
        if (searching) {
            searching = false
            boutViewModel.searchAllFighterBouts("")
        } else {
            searching = true
        }
        searchText = ""
    }

    MainScreenContent(
        bouts = if (searching) filtered else bouts,
        searching = searching,
        searchText = searchText,
        onSearchTextChange = onSearchTextChange,
        toggleSearch = toggleSearch,
        goToAddBout = goToAddBout,
        goToBoutScoring = goToBoutScoring,
        deleteBout = deleteBout
    )
}

/**
 * Content of the main screen
 */
@Composable
private fun MainScreenContent(
    bouts: List<ParsedBout>,
    searching: Boolean,
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    toggleSearch: () -> Unit,
    goToAddBout: () -> Unit,
    goToBoutScoring: (Int) -> Unit,
    deleteBout: (ParsedBout) -> Unit,
) {
    Scaffold(
        topBar = {
            MainTopBar(searching, searchText, onSearchTextChange, toggleSearch)
        },
        floatingActionButton = {
            FloatingButton(onClick = goToAddBout)
        }) { padding ->
        Surface(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
        ) {
            BoutList(bouts, goToBoutScoring, deleteBout)
        }
    }
}

/**
 * Top bar
 */
@Composable
private fun MainTopBar(
    searching: Boolean,
    searchText: String = "",
    onSearchTextChange: (String) -> Unit,
    toggleSearch: () -> Unit
) {
    Box {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 20.dp)
                .fillMaxWidth()
                .height(60.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AnimatedContent(
                targetState = searching,
                transitionSpec = {
                    expandIn() togetherWith shrinkOut()
                },
                label = "SearchTransition"
            ) { isSearching ->
                if (isSearching) {
                    val focusRequester = remember { FocusRequester() }

                    OutlinedTextField(
                        value = searchText,
                        onValueChange = onSearchTextChange,
                        placeholder = { Text(stringResource(R.string.search)) },
                        singleLine = true,
                        shape = RoundedCornerShape(50.dp),
                        textStyle = MaterialTheme.typography.bodyLarge,
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Words
                        ),
                        modifier = Modifier.focusRequester(focusRequester)
                    )

                    LaunchedEffect(Unit) {
                        focusRequester.requestFocus()
                    }
                } else {
                    Text(
                        stringResource(R.string.my_bouts_title),
                        fontWeight = FontWeight.ExtraBold,
                        style = MaterialTheme.typography.displaySmall
                    )
                }
            }

            Button(
                onClick = toggleSearch,
                shape = CircleShape,
                contentPadding = PaddingValues(0.dp),
                elevation = ButtonDefaults.buttonElevation(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.fillMaxHeight()
            ) {
                if (searching) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = stringResource(R.string.stop_search),
                    )
                } else {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = stringResource(R.string.search),
                    )
                }
            }
        }
    }
}

/**
 * Round floating action button to add a new bout
 */
@Composable
private fun FloatingButton(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
        shape = RoundedCornerShape(50.dp),
        containerColor = Color(59, 130, 246),
        contentColor = Color.White
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = stringResource(R.string.add_new_bout_title),
        )
    }
}

/**
 * Show a preview of the main screen
 */
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun MainScreenPreview() {
    val bout2 = ParsedBout(
        bout = Bout(
            rounds = 12,
            scores = mapOf(
                1 to Pair(9, 10),
                2 to Pair(9, 10),
                3 to Pair(9, 10),
                4 to Pair(10, 9),
                5 to Pair(10, 9),
                6 to Pair(10, 9),
                7 to Pair(9, 10),
                8 to Pair(9, 10),
                9 to Pair(9, 10),
                10 to Pair(9, 10),
                11 to Pair(9, 10),
                12 to Pair(10, 9)
            ),
            boutInfoId = "",
            redCornerId = "",
            blueCornerId = "",
            createdAt = Date(),
            updatedAt = Date()
        ),
        info = BoutInfo(
            winner = Winner.BLUE_CORNER,
            winMethod = WinMethod.MAJORITY_DECISION,
            weight = WeightClass.LIGHT_HEAVY,
            date = "2025-02-22",
            location = "Kingdom Arena, Riyadh",
            championship = true,
            notes = ""
        ),
        redCorner = Fighter(fullName = "Artur Beterbiev", displayName = "Beterbiev"),
        blueCorner = Fighter(fullName = "Dmitrii Bivol", displayName = "Bivol")
    )

    val bout3 = ParsedBout(
        bout = Bout(
            rounds = 12,
            scores = mapOf(
                1 to Pair(9, 10),
                2 to Pair(9, 10),
                3 to Pair(9, 10),
                4 to Pair(10, 8),
                5 to Pair(9, 10),
                6 to Pair(10, 9),
                7 to Pair(10, 8),
                8 to Pair(9, 10),
                9 to Pair(9, 10),
                10 to Pair(10, 9),
                11 to Pair(10, 9),
                12 to Pair(9, 10)
            ),
            boutInfoId = "",
            redCornerId = "",
            blueCornerId = "",
            createdAt = Date(),
            updatedAt = Date()
        ),
        info = BoutInfo(
            winner = Winner.DRAW,
            drawMethod = DrawMethod.MAJORITY_DRAW,
            weight = WeightClass.SUPER_LIGHT,
            date = "2023-11-16",
            location = "T-Mobile Arena, Las Vegas",
            championship = true,
            notes = "Conceicao down twice"
        ),
        redCorner = Fighter(fullName = " Emanuel Navarrete", displayName = "Navarrete"),
        blueCorner = Fighter(fullName = " Robson Conceicao", displayName = " Conceicao")
    )

    BoutScoringTheme(darkTheme = false) {
        MainScreenContent(
            bouts = listOf(ParsedBout.example(), bout2, bout3),
            searching = false,
            searchText = "",
            onSearchTextChange = {},
            goToAddBout = {},
            goToBoutScoring = {},
            deleteBout = {},
            toggleSearch = {}
        )
    }
}