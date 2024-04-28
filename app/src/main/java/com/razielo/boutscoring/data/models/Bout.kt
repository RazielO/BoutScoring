package com.razielo.boutscoring.data.models

data class Bout(
    val rounds: Int,
    val scores: Map<Int, Pair<Int, Int>>,
    val redCorner: Fighter,
    val blueCorner: Fighter
)
