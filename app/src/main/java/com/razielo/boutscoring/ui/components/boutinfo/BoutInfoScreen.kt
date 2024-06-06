package com.razielo.boutscoring.ui.components.boutinfo

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.razielo.boutscoring.R
import com.razielo.boutscoring.data.BoutViewModel
import com.razielo.boutscoring.ui.components.common.TopBar

@Composable
fun BoutInfoScreen(boutViewModel: BoutViewModel, goBack: () -> Unit) {
    val bout = boutViewModel.bout.value

    if (bout == null) {
        goBack()
    }

    Scaffold(topBar = {
        TopBar(
            titleText = stringResource(R.string.bout_info_title), goBack = true, onBack = goBack
        )
    }) { padding ->
        Surface(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            if (bout != null) {
                BoutInfoComponent(bout.info) {
                    boutViewModel.updateInfo(it)
                    boutViewModel.bout.value = bout.copy(info = it)
                }
            }
        }
    }
}