package com.razielo.boutscoring.data.models.enums

enum class DrawMethod(val displayName: String, val abbreviation: String) {
    TECHNICAL_DRAW("Technical Draw", "TD"),
    DRAW("Draw", "D"),
    SPLIT_DRAW("Split Draw", "SD"),
    MAJORITY_DRAW("Majority Draw", "MD");

    companion object {
        private val map = DrawMethod.entries.associateBy { it.displayName }

        infix fun fromDisplayName(displayName: String): DrawMethod? = map[displayName]
    }
}