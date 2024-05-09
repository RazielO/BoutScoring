package com.razielo.boutscoring

import android.app.Application
import com.razielo.boutscoring.data.BoutDatabase
import com.razielo.boutscoring.data.repository.BoutRepository

class BoutApplication : Application() {
    private val database by lazy { BoutDatabase.getDatabase(this) }

    val repository by lazy {
        BoutRepository(
            database.boutDao(),
            database.fighterDao(),
            database.boutFighterCrossRefDao()
        )
    }
}