package com.razielo.boutscoring.data.models

enum class NoResultMethod(val displayName: String, val abbreviation: String) {
    NO_DECISION("No Contest", "ND"),
    NO_CONTEST("No Contest", "NC");

    companion object {
        private val map = NoResultMethod.entries.associateBy { it.displayName }
        infix fun from(display: String) = map[display]
    }
}