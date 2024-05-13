package com.razielo.boutscoring.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

@Entity
data class Bout(
    @PrimaryKey @ColumnInfo(name = "bout_id") val id: String = UUID.randomUUID().toString(),
    val rounds: Int,
    val scores: Map<Int, Pair<Int, Int>>,
    @ColumnInfo(name = "bout_info_id") val boutInfoId: String,
    @ColumnInfo(name = "red_corner_id") val redCornerId: String,
    @ColumnInfo(name = "blue_corner_id") val blueCornerId: String,
    @ColumnInfo(name = "created_at") val createdAt: Date = Date(),
    @ColumnInfo(name = "updated_at") val updatedAt: Date = Date()
)
