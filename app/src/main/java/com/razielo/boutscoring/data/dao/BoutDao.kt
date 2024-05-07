package com.razielo.boutscoring.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.razielo.boutscoring.data.models.Bout
import kotlinx.coroutines.flow.Flow

@Dao
interface BoutDao {
    @Query("SELECT * FROM bout ORDER BY bout_id DESC")
    fun getAllBouts(): Flow<List<Bout>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(bout: Bout)

    @Update
    suspend fun update(bout: Bout)

    @Query("DELETE FROM bout WHERE bout_id = :id")
    suspend fun deleteBoutById(id: String)
}