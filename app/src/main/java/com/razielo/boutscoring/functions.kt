package com.razielo.boutscoring

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.razielo.boutscoring.data.models.BoutInfo
import com.razielo.boutscoring.ui.theme.greenContainerDark
import com.razielo.boutscoring.ui.theme.redContainerDark

fun scoreColors(scores: Pair<Int, Int>): Pair<Color, Color> =
    with(scores) {
        if (this.first > this.second) {
            Pair(greenContainerDark, redContainerDark)
        } else if (this.second > this.first) {
            Pair(redContainerDark, greenContainerDark)
        } else {
            Pair(Color.Gray, Color.Gray)
        }
    }

@Composable
fun BoutInfo.resultText(): String {
    val prefix = winner?.let { stringResource(it.abbreviation) } ?: ""

    return when {
        winner == null -> ""
        winMethod != null -> "$prefix-${stringResource(winMethod.abbreviation)}"
        drawMethod != null -> stringResource(drawMethod.abbreviation)
        noResultMethod != null -> stringResource(noResultMethod.abbreviation)
        else -> prefix
    }
}
