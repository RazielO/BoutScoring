package com.razielo.boutscoring.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

@Entity(tableName = "bout_info")
data class BoutInfo(
    @PrimaryKey @ColumnInfo(name = "bout_info_id") val id: String = UUID.randomUUID().toString(),
    val winner: Winner? = null,
    @ColumnInfo(name = "win_method") val winMethod: WinMethod? = null,
    @ColumnInfo(name = "draw_method") val drawMethod: DrawMethod? = null,
    @ColumnInfo(name = "no_result_method") val noResultMethod: NoResultMethod? = null,
    val date: Date? = null,
    val location: String = "",
    val championship: Boolean = false,
    val notes: String = ""
)
