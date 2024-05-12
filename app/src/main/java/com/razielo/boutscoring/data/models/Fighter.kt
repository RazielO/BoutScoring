package com.razielo.boutscoring.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Fighter(
    @PrimaryKey @ColumnInfo(name = "full_name") val fullName: String,
    @ColumnInfo(name = "display_name") val displayName: String,
)
