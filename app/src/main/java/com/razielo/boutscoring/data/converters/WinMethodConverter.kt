package com.razielo.boutscoring.data.converters

import androidx.room.TypeConverter
import com.razielo.boutscoring.data.models.WinMethod

class WinMethodConverter {
    @TypeConverter
    fun fromWinMethod(winner: WinMethod?): String? {
        return winner?.displayName
    }

    @TypeConverter
    fun toWinMethod(displayName: String?): WinMethod? {
        return displayName?.let { WinMethod.fromDisplayName(it) }
    }
}

