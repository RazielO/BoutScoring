package com.razielo.boutscoring

import androidx.compose.ui.graphics.Color
import com.razielo.boutscoring.data.models.Screen
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

fun topBarTitle(screen: Screen, boutCount: Int = 0, name: String = ""): String {
    return when (screen) {
        Screen.MAIN -> if (boutCount == 0) "My bouts" else "My $boutCount bouts"
        Screen.FILTERED_BOUTS -> "$name bouts"
        Screen.ADD_BOUT -> "Add new bout"
        Screen.SCORE_BOUT -> "Score bout"
    }
}
