package com.razielo.boutscoring.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class Fighter(
    val id: String = UUID.randomUUID().toString(),
    val fullName: String,
    val displayName: String,
) : Parcelable
