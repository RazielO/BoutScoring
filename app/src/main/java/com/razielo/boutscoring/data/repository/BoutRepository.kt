package com.razielo.boutscoring.data.repository

import androidx.annotation.WorkerThread
import com.razielo.boutscoring.data.dao.BoutDao
import com.razielo.boutscoring.data.models.Bout
import kotlinx.coroutines.flow.Flow

class BoutRepository(private val boutDao: BoutDao) {
    val bouts: Flow<List<Bout>> = boutDao.getAllBouts()

    @WorkerThread
    suspend fun insert(bout: Bout) {
        boutDao.insert(bout)
    }

    @WorkerThread
    suspend fun update(bout: Bout) {
        boutDao.update(bout)
    }

    @WorkerThread
    suspend fun deleteById(id: String) {
        boutDao.deleteBoutById(id)
    }
}