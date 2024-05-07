package com.razielo.boutscoring.data.converters

import androidx.room.TypeConverter
import com.razielo.boutscoring.data.models.Winner

class WinnerConverter {
    @TypeConverter
    fun fromWinner(winner: Winner?): String? {
        return winner?.displayName
    }

    @TypeConverter
    fun toWinner(displayName: String?): Winner? {
        return displayName?.let { Winner.fromDisplayName(it) }
    }
}

