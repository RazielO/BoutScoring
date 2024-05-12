package com.razielo.boutscoring.data.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Fighter(
    @PrimaryKey @ColumnInfo(name = "full_name") val fullName: String,
    @ColumnInfo(name = "display_name") val displayName: String,
) : Parcelable
