package com.razielo.boutscoring.data.models.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.razielo.boutscoring.data.models.Bout
import com.razielo.boutscoring.data.models.BoutInfo

data class BoutWithInfo(
    @Embedded val bout: Bout,
    @Relation(
        entityColumn = "bout_info_id",
        parentColumn = "bout_info_id"
    )
    val info: BoutInfo
)
