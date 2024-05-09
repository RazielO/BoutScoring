package com.razielo.boutscoring.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "bout_fighter_cross_ref",
    primaryKeys = ["bout_id", "fighter_id"],
//    foreignKeys = [
//        ForeignKey(entity = Bout::class, parentColumns = ["bout_id"], childColumns = ["bout_id"]),
//        ForeignKey(
//            entity = Fighter::class,
//            parentColumns = ["fighter_id"],
//            childColumns = ["fighter_id"]
//        )
//    ]
)
data class BoutFighterCrossRef(
    @ColumnInfo(name = "bout_id", index = true)
    val boutId: String,
    @ColumnInfo(name = "fighter_id", index = true)
    val fighterId: String
)

