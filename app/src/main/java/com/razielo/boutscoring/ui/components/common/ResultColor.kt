package com.razielo.boutscoring.ui.components.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.razielo.boutscoring.data.models.enums.Winner
import com.razielo.boutscoring.ui.theme.AppColors

@Composable
fun resultColor(winner: Winner?): Color {
    return when (winner) {
        Winner.RED_CORNER -> AppColors.Green
        Winner.BLUE_CORNER -> AppColors.Red
        Winner.DRAW, Winner.NO_RESULT -> Color.LightGray
        else -> Color.Transparent
    }
}
