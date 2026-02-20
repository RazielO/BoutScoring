package com.razielo.boutscoring.ui.components.boutinfo

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.razielo.boutscoring.R
import com.razielo.boutscoring.data.BoutViewModel
import com.razielo.boutscoring.data.models.BoutInfo
import com.razielo.boutscoring.ui.components.common.TopBar
import com.razielo.boutscoring.ui.models.ParsedBout
import com.razielo.boutscoring.ui.theme.BoutScoringTheme

@Composable
fun BoutInfoScreen(boutViewModel: BoutViewModel, goBack: () -> Unit) {
    val scrollState = rememberScrollState()
    val bout = boutViewModel.bout.value

    if (bout == null) {
        goBack()
    } else {
        val updateBout: (BoutInfo) -> Unit = {
            boutViewModel.updateInfo(it)
            boutViewModel.bout.value = bout.copy(info = it)
        }
        BoutInfoContent(bout, goBack, updateBout, scrollState)
    }
}

@Composable
private fun BoutInfoContent(
    bout: ParsedBout,
    goBack: () -> Unit,
    updateBout: (BoutInfo) -> Unit,
    scrollState: ScrollState
) {

    Scaffold(topBar = {
        BoutInfoTopBar(goBack)
    }) { padding ->
        Surface(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(scrollState),
        ) {
            BoutInfoComponent(bout.info, updateBout)
        }
    }
}

@Composable
fun BoutInfoTopBar(goBack: () -> Unit) {
    TopBar(stringResource(R.string.bout_info_title), goBack = goBack)
}

/**
 * Preview of the bout info screen
 */
@Preview
@Composable
private fun BoutInfoContentPreview() {
    val scrollState = rememberScrollState()

    BoutScoringTheme {
        BoutInfoContent(
            bout = ParsedBout.example(),
            goBack = {},
            updateBout = {},
            scrollState = scrollState
        )
    }
}
