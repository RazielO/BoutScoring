package com.razielo.boutscoring.data.models

enum class DrawMethod(displayName: String, val abbreviation: String) {
    TECHNICAL_DRAW("Technical Draw", "TD"),
    DRAW("Draw", "D"),
    SPLIT_DRAW("Split Draw", "SD"),
    MAJORITY_DRAW("Majority Draw", "MD")
}