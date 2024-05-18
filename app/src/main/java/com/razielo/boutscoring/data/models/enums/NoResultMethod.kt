package com.razielo.boutscoring.data.models.enums

enum class NoResultMethod(val displayName: String, val abbreviation: String) {
    NO_DECISION("No Decision", "ND"),
    NO_CONTEST("No Contest", "NC");

    companion object {
        private val map = NoResultMethod.entries.associateBy { it.displayName }

        infix fun fromDisplayName(displayName: String): NoResultMethod? = map[displayName]
    }
}