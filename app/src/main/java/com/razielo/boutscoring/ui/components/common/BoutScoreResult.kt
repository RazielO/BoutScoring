package com.razielo.boutscoring.ui.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.razielo.boutscoring.data.models.BoutInfo
import com.razielo.boutscoring.ui.components.boutscore.HeadText

@Composable
fun BoutScoreResult(info: BoutInfo, modifier: Modifier) {
    Box(
        modifier = modifier.clip(RoundedCornerShape(8.dp))
    ) {
        if (info.winner != null) {
            val text = info.resultText()
            val color = resultColor(info.winner)

            HeadText(
                text, Modifier
                    .fillMaxWidth()
                    .background(color), Color.Black
            )
        }
    }
}


