package com.razielo.boutscoring.data

import android.content.ContentValues
import android.content.Context
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.razielo.boutscoring.data.converters.DateConverter
import com.razielo.boutscoring.data.converters.ScoresConverter
import com.razielo.boutscoring.data.dao.BoutDao
import com.razielo.boutscoring.data.dao.BoutFighterCrossRefDao
import com.razielo.boutscoring.data.dao.FighterDao
import com.razielo.boutscoring.data.models.Bout
import com.razielo.boutscoring.data.models.BoutInfo
import com.razielo.boutscoring.data.models.Fighter
import com.razielo.boutscoring.data.models.relations.BoutFighterCrossRef
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Database(
    entities = [Bout::class, Fighter::class, BoutFighterCrossRef::class, BoutInfo::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(ScoresConverter::class, DateConverter::class)
abstract class BoutDatabase : RoomDatabase() {
    abstract fun boutDao(): BoutDao

    abstract fun fighterDao(): FighterDao

    abstract fun boutFighterCrossRefDao(): BoutFighterCrossRefDao

    companion object {
        @Volatile
        private var INSTANCE: BoutDatabase? = null

        private const val DB_NAME = "bout_database"
        private const val MIME_TYPE = "application/octet-stream"

        fun getDatabase(context: Context): BoutDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BoutDatabase::class.java,
                    DB_NAME
                ).build()
                INSTANCE = instance
                instance
            }
        }

        fun exportDatabase(context: Context) {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
            val today = current.format(formatter)

            val dbFile = context.getDatabasePath(DB_NAME)

            val contentResolver = context.contentResolver
            val downloadsUri = MediaStore.Downloads.EXTERNAL_CONTENT_URI
            val contentValues = ContentValues().apply {
                put(MediaStore.Downloads.DISPLAY_NAME, "${today}-bouts.db")
                put(MediaStore.Downloads.MIME_TYPE, MIME_TYPE)
                put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }

            val uri = contentResolver.insert(downloadsUri, contentValues)

            try {
                uri?.let {
                    contentResolver.openOutputStream(it)?.use { outputStream ->
                        dbFile.inputStream().use { inputStream ->
                            inputStream.copyTo(outputStream)
                        }
                    }
                    Toast.makeText(context, "Database Exported Successfully", Toast.LENGTH_SHORT)
                        .show()
                }.run {
                    Toast.makeText(context, "Failed to Export Database", Toast.LENGTH_SHORT).show()
                }
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(context, "Failed to Export Database", Toast.LENGTH_SHORT).show()
            }
        }
    }
}