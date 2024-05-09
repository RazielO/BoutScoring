package com.razielo.boutscoring.data.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
@Entity
data class Fighter(
    @PrimaryKey @ColumnInfo(name = "fighter_id") val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "full_name") val fullName: String,
    @ColumnInfo(name = "display_name") val displayName: String,
) : Parcelable
