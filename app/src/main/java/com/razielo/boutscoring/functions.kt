package com.razielo.boutscoring

import androidx.compose.ui.graphics.Color
import com.razielo.boutscoring.ui.theme.Green
import com.razielo.boutscoring.ui.theme.Red

fun scoreColors(scores: Pair<Int, Int>, default: Color): Pair<Color, Color> = with(scores) {
    if (this.first > this.second) {
        Pair(Green, Red)
    } else if (this.second > this.first) {
        Pair(Red, Green)
    } else {
        Pair(default, default)
    }
}
