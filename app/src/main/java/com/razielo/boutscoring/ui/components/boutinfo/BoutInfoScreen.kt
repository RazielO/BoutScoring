package com.razielo.boutscoring.ui.components.boutinfo

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.razielo.boutscoring.R
import com.razielo.boutscoring.data.BoutViewModel
import com.razielo.boutscoring.data.models.BoutInfo
import com.razielo.boutscoring.ui.models.ParsedBout

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
    Surface(shadowElevation = 8.dp) {
        Row(
            Modifier
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = goBack) {
                Icon(
                    Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = stringResource(R.string.go_back)
                )
            }
            Text(
                stringResource(R.string.bout_info_title).uppercase(),
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.titleMedium.fontSize
            )
        }
    }
}

/**
 * Preview of the bout info screen
 */
@Preview
@Composable
private fun BoutInfoContentPreview() {
    val scrollState = rememberScrollState()
    BoutInfoContent(
        bout = ParsedBout.example(),
        goBack = {},
        updateBout = {},
        scrollState = scrollState
    )
}
