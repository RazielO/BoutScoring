package com.razielo.boutscoring.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.razielo.boutscoring.data.converters.DateConverter
import com.razielo.boutscoring.data.converters.ScoresConverter
import com.razielo.boutscoring.data.dao.BoutDao
import com.razielo.boutscoring.data.models.Bout

@Database(entities = [Bout::class], version = 1, exportSchema = false)
@TypeConverters(ScoresConverter::class, DateConverter::class)
abstract class BoutDatabase : RoomDatabase() {
    abstract fun boutDao(): BoutDao

    companion object {
        @Volatile
        private var INSTANCE: BoutDatabase? = null

        fun getDatabase(context: Context): BoutDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BoutDatabase::class.java,
                    "bout_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}