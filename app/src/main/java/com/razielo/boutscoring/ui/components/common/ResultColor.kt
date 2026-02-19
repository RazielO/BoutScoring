package com.razielo.boutscoring.ui.components.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.razielo.boutscoring.data.models.enums.Winner
import com.razielo.boutscoring.ui.theme.greenContainerDark
import com.razielo.boutscoring.ui.theme.redContainerDark

@Composable
fun resultColor(winner: Winner?): Color {
    return when (winner) {
        Winner.RED_CORNER -> greenContainerDark
        Winner.BLUE_CORNER -> redContainerDark
        Winner.DRAW, Winner.NO_RESULT -> Color.LightGray
        else -> Color.Transparent
    }
}
