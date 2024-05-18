package com.razielo.boutscoring.data.models.enums

enum class WinMethod(val displayName: String, val abbreviation: String) {
    UNANIMOUS_DECISION("Unanimous Decision", "UD"),
    SPLIT_DECISION("Split Decision", "SD"),
    MAJORITY_DECISION("Majority Decision", "MD"),
    POINTS("Points", "PTS"),
    KNOCKOUT("Knockout", "KO"),
    TECHNICAL_KNOCKOUT("Technical Knockout", "TKO"),
    RETIRED("Retired", "RTD"),
    TECHNICAL_DECISION("Technical Decision", "TD"),
    DISQUALIFICATION("Disqualification", "DQ");

    companion object {
        private val map = WinMethod.entries.associateBy { it.displayName }

        infix fun fromDisplayName(displayName: String): WinMethod? = map[displayName]
    }
}