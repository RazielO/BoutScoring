package com.razielo.boutscoring.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "bout_fighter_cross_ref",
    primaryKeys = ["bout_id", "full_name"],
    foreignKeys = [ForeignKey(
        entity = Bout::class,
        parentColumns = ["bout_id"],
        childColumns = ["bout_id"],
        onDelete = ForeignKey.CASCADE
    ), ForeignKey(
        entity = Fighter::class,
        parentColumns = ["full_name"],
        childColumns = ["full_name"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class BoutFighterCrossRef(
    @ColumnInfo(name = "bout_id", index = true) val boutId: String,
    @ColumnInfo(name = "full_name", index = true) val fullName: String
)

