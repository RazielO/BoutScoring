package com.razielo.boutscoring.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.razielo.boutscoring.data.models.Fighter
import com.razielo.boutscoring.data.models.FighterWithBouts

@Dao
interface FighterDao {
    @Insert
    suspend fun insert(fighter: Fighter): Long

    @Transaction
    @Query("SELECT * FROM fighter WHERE fighter_id = :id")
    suspend fun getAllFighterBouts(id: String): List<FighterWithBouts>

    @Query("SELECT * FROM Fighter WHERE fighter_id = :fighterId")
    suspend fun getFighterById(fighterId: String): Fighter?

    @Query("SELECT * FROM Fighter WHERE full_name = :name")
    suspend fun getFighterByName(name: String): Fighter?

    @Query("DELETE FROM fighter WHERE fighter_id = :id")
    suspend fun deleteFighterById(id: String)
}
