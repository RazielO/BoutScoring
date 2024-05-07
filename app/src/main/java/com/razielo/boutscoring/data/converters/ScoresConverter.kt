package com.razielo.boutscoring.data.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ScoresConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromScores(map: Map<Int, Pair<Int, Int>>): String {
        return gson.toJson(map)
    }

    @TypeConverter
    fun toScores(json: String): Map<Int, Pair<Int, Int>> {
        val type = object : TypeToken<Map<Int, Pair<Int, Int>>>() {}.type
        return gson.fromJson(json, type)
    }
}