package com.razielo.boutscoring.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.razielo.boutscoring.data.models.enums.DrawMethod
import com.razielo.boutscoring.data.models.enums.NoResultMethod
import com.razielo.boutscoring.data.models.enums.WeightClass
import com.razielo.boutscoring.data.models.enums.WinMethod
import com.razielo.boutscoring.data.models.enums.Winner
import java.util.UUID

@Entity(tableName = "bout_info")
data class BoutInfo(
    @PrimaryKey @ColumnInfo(name = "bout_info_id") val id: String = UUID.randomUUID().toString(),
    val winner: Winner? = null,
    @ColumnInfo(name = "win_method") val winMethod: WinMethod? = null,
    @ColumnInfo(name = "draw_method") val drawMethod: DrawMethod? = null,
    @ColumnInfo(name = "no_result_method") val noResultMethod: NoResultMethod? = null,
    val weight: WeightClass? = null,
    val date: String? = null,
    val location: String = "",
    val championship: Boolean = false,
    val notes: String = ""
)
