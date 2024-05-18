package com.razielo.boutscoring.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.razielo.boutscoring.data.models.Bout
import com.razielo.boutscoring.data.models.BoutInfo
import com.razielo.boutscoring.data.models.relations.BoutWithFighters
import kotlinx.coroutines.flow.Flow

@Dao
interface BoutDao {
    @Transaction
    @Query("SELECT * FROM bout ORDER BY created_at DESC")
    fun getAllBouts(): Flow<List<BoutWithFighters>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(bout: Bout)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertInfo(info: BoutInfo)

    @Transaction
    @Query("SELECT * FROM bout WHERE bout_id = :boutId")
    suspend fun getBoutById(boutId: String): BoutWithFighters?

    @Update
    suspend fun update(bout: Bout)

    @Update
    suspend fun updateInfo(info: BoutInfo)

    @Query("DELETE FROM bout_info WHERE bout_info_id = :id")
    suspend fun deleteBoutInfoById(id: String)

    @Query("DELETE FROM bout WHERE bout_id = :id")
    suspend fun deleteBoutById(id: String)

    @Transaction
    @Query("SELECT * FROM bout WHERE bout_id IN (SELECT bout_id FROM bout_fighter_cross_ref WHERE full_name = :id) ORDER BY created_at DESC")
    suspend fun getAllFighterBouts(id: String): List<BoutWithFighters>

    @Transaction
    @Query("SELECT * FROM bout WHERE bout_id IN (SELECT bout_id FROM bout_fighter_cross_ref WHERE full_name IN (SELECT full_name FROM fighter WHERE full_name LIKE :pattern)) ORDER BY created_at DESC")
    suspend fun searchAllFighterBouts(pattern: String): List<BoutWithFighters>
}