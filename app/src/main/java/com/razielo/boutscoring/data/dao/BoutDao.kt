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
    @Transaction
    @Query("SELECT * FROM bout ORDER BY created_at DESC")
    fun getAllBouts(): Flow<List<BoutWithFighters>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(bout: Bout)

    @Transaction
    @Query("SELECT * FROM bout WHERE bout_id = :boutId")
    suspend fun getBoutById(boutId: String): BoutWithFighters?

    @Update
    suspend fun update(bout: Bout)

    @Query("DELETE FROM bout WHERE bout_id = :id")
    suspend fun deleteBoutById(id: String)

    @Transaction
    @Query("SELECT * FROM bout WHERE bout_id IN (SELECT bout_id FROM bout_fighter_cross_ref WHERE fighter_id = :id)")
    suspend fun getAllFighterBouts(id: String): List<BoutWithFighters>

    @Transaction
    @Query(
        "SELECT * FROM bout WHERE bout_id IN " +
                "(SELECT bout_id FROM bout_fighter_cross_ref WHERE fighter_id IN " +
                "(SELECT fighter_id FROM fighter WHERE full_name LIKE :pattern))"
    )
    suspend fun searchAllFighterBouts(pattern: String): List<BoutWithFighters>
}