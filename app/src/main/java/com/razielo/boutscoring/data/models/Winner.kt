package com.razielo.boutscoring.data.models

enum class Winner(val display: String) {
    RED_CORNER("Red Corner"),
    BLUE_CORNER("Blue Corner"),
    DRAW("Draw"),
    NO_RESULT("No Result");

    companion object {
        private val map = entries.associateBy { it.display }
        infix fun from(display: String) = map[display]
    }
}