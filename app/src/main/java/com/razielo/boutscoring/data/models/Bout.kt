package com.razielo.boutscoring.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class Bout(
    val id: String = UUID.randomUUID().toString(),
    val rounds: Int,
    var scores: Map<Int, Pair<Int, Int>>,
    val redCorner: Fighter,
    val blueCorner: Fighter,
    var winner: Winner? = null,
    var winMethod: WinMethod? = null,
    var drawMethod: DrawMethod? = null,
    var noResultMethod: NoResultMethod? = null
) : Parcelable
