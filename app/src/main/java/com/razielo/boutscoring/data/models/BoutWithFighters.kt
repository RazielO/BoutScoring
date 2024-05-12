package com.razielo.boutscoring.data.models

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class BoutWithFighters(
    @Embedded val bout: Bout,
    @Relation(
        parentColumn = "bout_id",
        entityColumn = "full_name",
        associateBy = Junction(BoutFighterCrossRef::class)
    )
    val fighters: List<Fighter>
)

