package com.razielo.boutscoring.data.repository

import androidx.annotation.WorkerThread
import com.razielo.boutscoring.data.dao.BoutDao
import com.razielo.boutscoring.data.dao.BoutFighterCrossRefDao
import com.razielo.boutscoring.data.dao.FighterDao
import com.razielo.boutscoring.data.models.BoutFighterCrossRef
import com.razielo.boutscoring.data.models.BoutWithFighters
import com.razielo.boutscoring.data.models.ParsedBout
import kotlinx.coroutines.flow.Flow

class BoutRepository(
    private val boutDao: BoutDao,
    private val fighterDao: FighterDao,
    private val boutFighterCrossRefDao: BoutFighterCrossRefDao
) {
    val bouts: Flow<List<BoutWithFighters>> = boutDao.getAllBouts()

    @WorkerThread
    suspend fun insert(bout: ParsedBout) {
        fighterDao.insert(bout.redCorner)
        fighterDao.insert(bout.blueCorner)
        boutDao.insertInfo(bout.info)
        boutDao.insert(bout.bout)
        boutFighterCrossRefDao.insert(BoutFighterCrossRef(bout.bout.id, bout.redCorner.fullName))
        boutFighterCrossRefDao.insert(BoutFighterCrossRef(bout.bout.id, bout.blueCorner.fullName))
    }

    @WorkerThread
    suspend fun getBoutById(id: String): ParsedBout? {
        return boutDao.getBoutById(id)?.let { ParsedBout.fromBoutWithFighters(it) }
    }

    @WorkerThread
    suspend fun getAllFighterBouts(name: String): List<ParsedBout> {
        return boutDao.getAllFighterBouts(name).mapNotNull { ParsedBout.fromBoutWithFighters(it) }
    }

    @WorkerThread
    suspend fun searchAllFighterBouts(pattern: String): List<ParsedBout> {
        return boutDao.searchAllFighterBouts(pattern)
            .mapNotNull { ParsedBout.fromBoutWithFighters(it) }
    }

    @WorkerThread
    suspend fun update(bout: ParsedBout) {
        boutDao.update(bout.bout)
        boutDao.updateInfo(bout.info)
    }

    @WorkerThread
    suspend fun deleteBout(bout: ParsedBout) {
        boutDao.deleteBoutById(bout.bout.id)
        boutDao.deleteBoutInfoById(bout.info.id)
        if (fighterDao.getAllFighterBouts(bout.redCorner.fullName).isEmpty()) {
            fighterDao.deleteFighter(bout.redCorner.fullName)
        }
        if (fighterDao.getAllFighterBouts(bout.blueCorner.fullName).isEmpty()) {
            fighterDao.deleteFighter(bout.blueCorner.fullName)
        }
    }
}