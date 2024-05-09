package com.razielo.boutscoring.data.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.Date
import java.util.UUID

@Parcelize
@Entity
data class Bout(
    @PrimaryKey @ColumnInfo(name = "bout_id") val id: String = UUID.randomUUID().toString(),
    val rounds: Int,
    val scores: Map<Int, Pair<Int, Int>>,
    val winner: Winner? = null,
    @ColumnInfo(name = "win_method") val winMethod: WinMethod? = null,
    @ColumnInfo(name = "draw_method") val drawMethod: DrawMethod? = null,
    @ColumnInfo(name = "no_result_method") val noResultMethod: NoResultMethod? = null,
    @ColumnInfo(name = "created_at") val createdAt: Date = Date(),
    @ColumnInfo(name = "updated_at") val updatedAt: Date = Date()
) : Parcelable
