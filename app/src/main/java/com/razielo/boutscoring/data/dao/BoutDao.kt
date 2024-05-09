package com.razielo.boutscoring.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.razielo.boutscoring.data.models.Bout
import com.razielo.boutscoring.data.models.BoutWithFighters
import kotlinx.coroutines.flow.Flow

@Dao
interface BoutDao {
    @Query("SELECT * FROM bout ORDER BY created_at DESC")
    fun getAllBouts(): Flow<List<BoutWithFighters>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(bout: Bout)

    @Transaction
    @Query("SELECT * FROM bout WHERE bout_id = :boutId")
    suspend fun getBoutWithFighters(boutId: Long): BoutWithFighters?

    @Update
    suspend fun update(bout: Bout)

    @Query("DELETE FROM bout WHERE bout_id = :id")
    suspend fun deleteBoutById(id: String)
}