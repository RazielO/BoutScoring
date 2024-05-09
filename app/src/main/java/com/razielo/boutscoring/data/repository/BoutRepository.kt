package com.razielo.boutscoring.data.repository

import androidx.annotation.WorkerThread
import com.razielo.boutscoring.data.dao.BoutDao
import com.razielo.boutscoring.data.dao.BoutFighterCrossRefDao
import com.razielo.boutscoring.data.dao.FighterDao
import com.razielo.boutscoring.data.models.Bout
import com.razielo.boutscoring.data.models.BoutFighterCrossRef
import com.razielo.boutscoring.data.models.BoutWithFighters
import com.razielo.boutscoring.data.models.Fighter
import kotlinx.coroutines.flow.Flow

class BoutRepository(
    private val boutDao: BoutDao,
    private val fighterDao: FighterDao,
    private val boutFighterCrossRefDao: BoutFighterCrossRefDao
) {
    val bouts: Flow<List<BoutWithFighters>> = boutDao.getAllBouts()

    @WorkerThread
    suspend fun insert(bout: BoutWithFighters) {
        val redId = insertOrGetFighter(bout.fighters[0])
        val blueId = insertOrGetFighter(bout.fighters[1])
        boutDao.insert(bout.bout)
        boutFighterCrossRefDao.insert(BoutFighterCrossRef(bout.bout.id, redId))
        boutFighterCrossRefDao.insert(BoutFighterCrossRef(bout.bout.id, blueId))
    }

    @WorkerThread
    suspend fun getBoutById(id: String): BoutWithFighters? {
        return boutDao.getBoutById(id)
    }

    @WorkerThread
    suspend fun getAllFighterBouts(id: String): List<BoutWithFighters> {
        return boutDao.getAllFighterBouts(id)
    }

    @WorkerThread
    suspend fun searchAllFighterBouts(pattern: String): List<BoutWithFighters> {
        return boutDao.searchAllFighterBouts(pattern)
    }

    private suspend fun insertOrGetFighter(fighter: Fighter): String {
        val existingFighter = fighterDao.getFighterByName(fighter.fullName)
        return if (existingFighter != null) {
            existingFighter.id
        } else {
            fighterDao.insert(fighter)
            fighter.id
        }
    }

    @WorkerThread
    suspend fun update(bout: Bout) {
        boutDao.update(bout)
    }

    @WorkerThread
    suspend fun deleteBout(bout: BoutWithFighters) {
        boutDao.deleteBoutById(bout.bout.id)
        if (fighterDao.getAllFighterBouts(bout.fighters[0].id).isEmpty()) {
            fighterDao.deleteFighterById(bout.fighters[0].id)
        }
        if (fighterDao.getAllFighterBouts(bout.fighters[1].id).isEmpty()) {
            fighterDao.deleteFighterById(bout.fighters[1].id)
        }
    }
}