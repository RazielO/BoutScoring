package com.razielo.boutscoring.ui.components.boutscore

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.razielo.boutscoring.R
import com.razielo.boutscoring.data.BoutViewModel
import com.razielo.boutscoring.scoreColors
import com.razielo.boutscoring.ui.components.common.Pill
import com.razielo.boutscoring.ui.components.common.resultColor
import com.razielo.boutscoring.ui.models.ParsedBout
import com.razielo.boutscoring.ui.theme.BoutScoringTheme
import kotlinx.coroutines.launch

@Composable
fun BoutScoreScreen(boutViewModel: BoutViewModel, goBack: () -> Unit, goToInfo: () -> Unit) {
    val snackbarHostState = remember { SnackbarHostState() }
    var bout by remember { mutableStateOf(boutViewModel.bout.value) }

    val updateBout: (ParsedBout) -> Unit = {
        bout = it
        boutViewModel.update(it)
        boutViewModel.bout.value = it
    }

    if (bout == null) {
        goBack()
    } else {
        BoutScoreContent(bout!!, updateBout, snackbarHostState, goToInfo, goBack)
    }
}

@Composable
private fun BoutScoreContent(
    bout: ParsedBout,
    updateBout: (ParsedBout) -> Unit,
    snackbarHostState: SnackbarHostState,
    goToInfo: () -> Unit,
    goBack: () -> Unit
) {
    var selectedIndex by remember { mutableIntStateOf(0) }

    val scope = rememberCoroutineScope()
    val errorMsg = stringResource(R.string.score_round_error)
    val showErrorSnackbar = {
        scope.launch {
            snackbarHostState.showSnackbar(errorMsg)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = { BoutScoreTopBar(goBack, goToInfo) }
    ) { padding ->
        Surface(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                ContentSwitcher(
                    listOf("Carousel View", "List View"),
                    selectedIndex,
                    onSelectionChange = { selectedIndex = it },
                )
                // Names row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    HeadText(bout.redCorner.displayName, Modifier.weight(2f))
                    if (bout.info.championship) {
                        Image(
                            painterResource(R.drawable.belt),
                            contentDescription = stringResource(R.string.championship_bout),
                            modifier = Modifier
                                .height(28.dp)
                                .weight(1f)
                        )
                    } else {
                        Text(
                            stringResource(R.string.vs).uppercase(),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            fontStyle = FontStyle.Italic,
                            color = Color.Gray
                        )
                    }
                    HeadText(bout.blueCorner.displayName, Modifier.weight(2f))
                }
                // Scores row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 20.dp)
                ) {
                    val redScore = bout.bout.scores.values.sumOf { it.first }
                    val blueScore = bout.bout.scores.values.sumOf { it.second }

                    val colors =
                        scoreColors(
                            Pair(redScore, blueScore),
                            Color.Gray
                        )

                    ScoreText(
                        redScore,
                        Modifier.weight(2f),
                        colors.first
                    )
                    Pill(
                        text = bout.info.resultText(),
                        background = resultColor(bout.info.winner),
                        modifier = Modifier.weight(1f)
                    )
                    ScoreText(
                        blueScore,
                        Modifier.weight(2f),
                        colors.second
                    )
                }

                AnimatedContent(
                    targetState = selectedIndex == 0,
                    label = "BoutScoreScreen",
                    transitionSpec = {
                        if (targetState > initialState) {
                            // Sliding to the right (next screen)
                            slideInHorizontally(
                                initialOffsetX = { fullWidth -> fullWidth },
                                animationSpec = tween(300)
                            ) + fadeIn(animationSpec = tween(300)) togetherWith
                                    slideOutHorizontally(
                                        targetOffsetX = { fullWidth -> -fullWidth },
                                        animationSpec = tween(300)
                                    ) + fadeOut(animationSpec = tween(300))
                        } else {
                            // Sliding to the left (previous screen)
                            slideInHorizontally(
                                initialOffsetX = { fullWidth -> -fullWidth },
                                animationSpec = tween(300)
                            ) + fadeIn(animationSpec = tween(300)) togetherWith
                                    slideOutHorizontally(
                                        targetOffsetX = { fullWidth -> fullWidth },
                                        animationSpec = tween(300)
                                    ) + fadeOut(animationSpec = tween(300))
                        }
                    },
                ) { carousel ->
                    if (carousel) {
                        BoutScoreCarousel(
                            bout,
                            showErrorSnackbar,
                            updateBout
                        )
                    } else {
                        BoutScoreList(
                            bout,
                            showErrorSnackbar,
                            updateBout
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ScoreText(
    score: Int,
    modifier: Modifier,
    color: Color
) {
    Text(
        score.toString(),
        textAlign = TextAlign.Center,
        modifier = modifier,
        fontWeight = FontWeight.ExtraBold,
        style = MaterialTheme.typography.headlineLarge,
        color = color
    )
}

@Composable
private fun HeadText(
    text: String,
    modifier: Modifier,
    color: Color = MaterialTheme.colorScheme.onBackground
) {
    Text(
        text.uppercase(),
        textAlign = TextAlign.Center,
        modifier = modifier,
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold,
        color = color
    )
}

@Composable
private fun ContentSwitcher(
    options: List<String>,
    selectedIndex: Int,
    onSelectionChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    activeColor: Color = MaterialTheme.colorScheme.primaryContainer,
    inactiveColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    activeTextColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    inactiveTextColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
    backgroundColor: Color = MaterialTheme.colorScheme.secondaryContainer
) {
    Box(
        modifier = modifier
            .wrapContentSize()
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(backgroundColor)
                .padding(horizontal = 4.dp, vertical = 4.dp)
                .fillMaxWidth(0.8f)
        ) {
            options.forEachIndexed { index, option ->
                val isSelected = index == selectedIndex

                val itemBackgroundColor by animateColorAsState(
                    targetValue = if (isSelected) activeColor else inactiveColor,
                    animationSpec = tween(300),
                    label = "itemBackgroundColor"
                )

                val textColor by animateColorAsState(
                    targetValue = if (isSelected) activeTextColor else inactiveTextColor,
                    animationSpec = tween(300),
                    label = "textColor"
                )

                val shape = RoundedCornerShape(6.dp)

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .then(
                            if (isSelected) {
                                Modifier.shadow(
                                    elevation = 2.dp,
                                    shape = shape,
                                    clip = false
                                )
                            } else {
                                Modifier
                            }
                        )
                        .background(itemBackgroundColor, shape)
                        .clip(RoundedCornerShape(6.dp))
                        .clickable { onSelectionChange(index) }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = option,
                        color = textColor,
                        fontSize = 15.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                    )
                }
            }
        }
    }
}

@Composable
private fun BoutScoreTopBar(goBack: () -> Unit, goToInfo: () -> Unit) {
    Surface(shadowElevation = 8.dp) {
        Row(
            Modifier
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = goBack) {
                    Icon(
                        Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = stringResource(R.string.go_back)
                    )
                }
                Text(
                    stringResource(R.string.score_bout_title).uppercase(),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
                )
            }

            IconButton(onClick = goToInfo) {
                Icon(
                    Icons.Default.Info,
                    contentDescription = stringResource(R.string.bout_info_title)
                )
            }
        }
    }
}

@Preview
@Composable
private fun BoutScoreScreenPreview() {
    val snackbarHostState = remember { SnackbarHostState() }

    BoutScoringTheme {
        BoutScoreContent(
            ParsedBout.example(),
            {},
            snackbarHostState,
            {}
        ) { }
    }
}