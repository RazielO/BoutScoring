package com.razielo.boutscoring.data.models.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.razielo.boutscoring.data.models.Bout
import com.razielo.boutscoring.data.models.Fighter

data class FighterWithBouts(
    @Embedded val fighter: Fighter,
    @Relation(
        parentColumn = "full_name",
        entityColumn = "bout_id",
        associateBy = Junction(BoutFighterCrossRef::class)
    )
    val bouts: List<Bout>
)

