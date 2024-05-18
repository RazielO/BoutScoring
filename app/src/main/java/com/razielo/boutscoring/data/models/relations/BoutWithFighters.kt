package com.razielo.boutscoring.data.models.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.razielo.boutscoring.data.models.Fighter

data class BoutWithFighters(
    @Embedded val bout: BoutWithInfo,
    @Relation(
        parentColumn = "bout_id",
        entityColumn = "full_name",
        associateBy = Junction(BoutFighterCrossRef::class)
    )
    val fighters: List<Fighter>
)

