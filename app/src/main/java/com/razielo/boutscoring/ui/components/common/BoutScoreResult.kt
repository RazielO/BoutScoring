package com.razielo.boutscoring.ui.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.razielo.boutscoring.data.models.BoutInfo
import com.razielo.boutscoring.data.models.enums.DrawMethod
import com.razielo.boutscoring.data.models.enums.NoResultMethod
import com.razielo.boutscoring.data.models.enums.WinMethod
import com.razielo.boutscoring.data.models.enums.Winner
import com.razielo.boutscoring.ui.components.boutscore.HeadText
import com.razielo.boutscoring.ui.theme.Green
import com.razielo.boutscoring.ui.theme.Red

@Composable
fun BoutScoreResult(info: BoutInfo, modifier: Modifier) {
    Box(
        modifier = modifier.clip(RoundedCornerShape(8.dp))
    ) {
        if (info.winner != null) {
            val text = when (info.winner) {
                Winner.RED_CORNER, Winner.BLUE_CORNER -> resultText(info.winner, info.winMethod)
                Winner.DRAW -> resultText(info.winner, info.drawMethod)
                Winner.NO_RESULT -> resultText(info.winner, info.noResultMethod)
            }
            val color = resultColor(info.winner) ?: MaterialTheme.colorScheme.background

            HeadText(
                text,
                Modifier
                    .fillMaxWidth()
                    .background(color),
                Color.Black
            )
        }
    }
}

private fun resultText(winner: Winner?, method: Any?): String {
    val prefix = when (winner) {
        null -> ""
        else -> winner.abbreviation
    }

    return when (winner) {
        null -> ""
        else -> when (method) {
            null -> prefix
            is WinMethod -> "$prefix-${method.abbreviation}"
            is DrawMethod -> when (prefix) {
                "" -> "D"
                else -> method.abbreviation
            }

            is NoResultMethod -> when (prefix) {
                "" -> "NC"
                else -> method.abbreviation
            }

            else -> ""
        }
    }
}

private fun resultColor(winner: Winner?): Color? = when (winner) {
    null -> null
    Winner.RED_CORNER -> Green
    Winner.BLUE_CORNER -> Red
    Winner.DRAW, Winner.NO_RESULT -> Color.LightGray
}
