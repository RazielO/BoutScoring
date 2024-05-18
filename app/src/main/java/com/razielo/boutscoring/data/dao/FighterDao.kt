package com.razielo.boutscoring.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.razielo.boutscoring.data.models.Fighter
import com.razielo.boutscoring.data.models.relations.FighterWithBouts

@Dao
interface FighterDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(fighter: Fighter): Long

    @Transaction
    @Query("SELECT * FROM fighter WHERE full_name = :id")
    suspend fun getAllFighterBouts(id: String): List<FighterWithBouts>

    @Query("SELECT * FROM Fighter WHERE full_name = :name")
    suspend fun getFighterByName(name: String): Fighter?

    @Query("DELETE FROM fighter WHERE full_name = :id")
    suspend fun deleteFighter(id: String)
}
