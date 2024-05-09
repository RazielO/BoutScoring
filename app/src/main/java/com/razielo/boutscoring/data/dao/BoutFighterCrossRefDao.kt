package com.razielo.boutscoring.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.razielo.boutscoring.data.models.BoutFighterCrossRef

@Dao
interface BoutFighterCrossRefDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(crossRef: BoutFighterCrossRef)
}