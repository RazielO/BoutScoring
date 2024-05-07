package com.razielo.boutscoring.data.models

enum class Winner(val displayName: String, val abbreviation: String) {
    RED_CORNER("Red Corner", "W"),
    BLUE_CORNER("Blue Corner", "L"),
    DRAW("Draw", "D"),
    NO_RESULT("No Result", "NC");

    companion object {
        private val map = entries.associateBy { it.displayName }

        infix fun fromDisplayName(displayName: String): Winner? = map[displayName]
    }
}