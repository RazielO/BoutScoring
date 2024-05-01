package com.razielo.boutscoring

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.razielo.boutscoring.ui.components.addbout.AddBoutComponent
import com.razielo.boutscoring.ui.theme.BoutScoringTheme

class AddBoutActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BoutScoringTheme {
                AddBoutComponent(this) { finish() }
            }
        }
    }
}
