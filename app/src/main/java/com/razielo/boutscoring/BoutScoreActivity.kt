package com.razielo.boutscoring

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.razielo.boutscoring.data.models.Bout
import com.razielo.boutscoring.ui.components.boutscore.BoutScoreComponent

class BoutScoreActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bout: Bout = intent.getParcelableExtra("bout", Bout::class.java) as Bout

        setContent {
            BoutScoreComponent(bout) {
                finish()
            }
        }
    }
}
