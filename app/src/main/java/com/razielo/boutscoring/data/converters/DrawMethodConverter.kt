package com.razielo.boutscoring.data.converters

import androidx.room.TypeConverter
import com.razielo.boutscoring.data.models.DrawMethod

class DrawMethodConverter {
    @TypeConverter
    fun fromDrawMethod(winner: DrawMethod?): String? {
        return winner?.displayName
    }

    @TypeConverter
    fun toDrawMethod(displayName: String?): DrawMethod? {
        return displayName?.let { DrawMethod.fromDisplayName(it) }
    }
}

