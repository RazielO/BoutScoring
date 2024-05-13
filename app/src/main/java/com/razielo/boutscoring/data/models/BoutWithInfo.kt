package com.razielo.boutscoring.data.models

import androidx.room.Embedded
import androidx.room.Relation

data class BoutWithInfo(
    @Embedded val bout: Bout,
    @Relation(
        entityColumn = "bout_info_id",
        parentColumn = "bout_info_id"
    )
    val info: BoutInfo
)
