package com.razielo.boutscoring.data.models

data class ParsedBout(
    val bout: Bout,
    val redCorner: Fighter,
    val blueCorner: Fighter
) {
    companion object {
        fun fromBoutWithFighters(withFighters: BoutWithFighters): ParsedBout? {
            if (withFighters.fighters.size != 2) {
                return null
            }

            val bout = withFighters.bout
            val redCorner = withFighters.fighters.first { it.fullName == bout.redCornerId }
            val blueCorner = withFighters.fighters.first { it.fullName == bout.blueCornerId }

            return ParsedBout(bout, redCorner, blueCorner)
        }
    }
}

