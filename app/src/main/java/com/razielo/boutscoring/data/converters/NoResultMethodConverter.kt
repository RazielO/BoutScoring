package com.razielo.boutscoring.data.converters

import androidx.room.TypeConverter
import com.razielo.boutscoring.data.models.NoResultMethod

class NoResultMethodConverter {
    @TypeConverter
    fun fromNoResultMethod(winner: NoResultMethod?): String? {
        return winner?.displayName
    }

    @TypeConverter
    fun toNoResultMethod(displayName: String?): NoResultMethod? {
        return displayName?.let { NoResultMethod.fromDisplayName(it) }
    }
}

