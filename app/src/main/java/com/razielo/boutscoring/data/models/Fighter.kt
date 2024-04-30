package com.razielo.boutscoring.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Fighter(
    val fullName: String,
    val displayName: String,
) : Parcelable
