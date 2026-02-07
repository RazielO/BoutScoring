package com.razielo.boutscoring.ui.models

import com.razielo.boutscoring.data.models.Bout
import com.razielo.boutscoring.data.models.BoutInfo
import com.razielo.boutscoring.data.models.Fighter
import com.razielo.boutscoring.data.models.enums.WeightClass
import com.razielo.boutscoring.data.models.enums.WinMethod
import com.razielo.boutscoring.data.models.enums.Winner
import com.razielo.boutscoring.data.models.relations.BoutWithFighters
import java.util.Date

/**
 * Data class for a bout that is parsed from the database
 */
data class ParsedBout(
    val bout: Bout,
    val info: BoutInfo,
    val redCorner: Fighter,
    val blueCorner: Fighter
) {
    companion object {
        fun fromBoutWithFighters(withFighters: BoutWithFighters): ParsedBout? {
            if (withFighters.fighters.size != 2) {
                return null
            }

            val bout = withFighters.bout.bout
            val info = withFighters.bout.info
            val redCorner = withFighters.fighters.first { it.fullName == bout.redCornerId }
            val blueCorner = withFighters.fighters.first { it.fullName == bout.blueCornerId }

            return ParsedBout(bout, info, redCorner, blueCorner)
        }

        fun example(): ParsedBout {
            val boutInfo = BoutInfo(
                winner = Winner.RED_CORNER,
                winMethod = WinMethod.SPLIT_DECISION,
                weight = WeightClass.HEAVY,
                date = "2024-05-18",
                location = "Kingdom Arena, Riyadh",
                championship = true,
                notes = "Fury ruled down in round nine"
            )

            val redCorner = Fighter(fullName = "Oleksandr Usyk", displayName = "Usyk")
            val blueCorner = Fighter(fullName = "Tyson Fury", displayName = "Fury")

            val bout = Bout(
                rounds = 12,
                scores = mapOf(
                    1 to Pair(10, 9),
                    2 to Pair(10, 9),
                    3 to Pair(9, 10),
                    4 to Pair(9, 10),
                    5 to Pair(9, 10),
                    6 to Pair(9, 10),
                    7 to Pair(9, 10),
                    8 to Pair(10, 9),
                    9 to Pair(10, 8),
                    10 to Pair(10, 9),
                    11 to Pair(10, 9),
                    12 to Pair(9, 10)
                ),
                boutInfoId = boutInfo.id,
                redCornerId = redCorner.displayName,
                blueCornerId = blueCorner.displayName,
                createdAt = Date(),
                updatedAt = Date()
            )

            return ParsedBout(bout, boutInfo, redCorner, blueCorner)
        }


    }
}

