package com.razielo.boutscoring.ui.components.main

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.razielo.boutscoring.BoutScoreActivity
import com.razielo.boutscoring.data.models.Bout
import com.razielo.boutscoring.scoreColors
import com.razielo.boutscoring.ui.components.common.BoutScoreResult

@Composable
fun BoutListCard(context: Context, bout: Bout) {
    Card(modifier = Modifier.fillMaxWidth()) {
        val redScore = bout.scores.values.sumOf { it.first }
        val blueScore = bout.scores.values.sumOf { it.second }
        val colors = scoreColors(
            Pair(redScore, blueScore),
            MaterialTheme.colorScheme.onSurfaceVariant
        )

        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(7f)) {
                Row {
                    CardText(bout.redCorner.fullName, Modifier.weight(5f))
                    CardText(redScore.toString(), Modifier.weight(1f), colors.first)
                }
                Row {
                    CardText("vs", Modifier.weight(3f))
                    BoutScoreResult(bout, Modifier.weight(1f))
                }
                Row {
                    CardText(bout.blueCorner.fullName, Modifier.weight(5f))
                    CardText(blueScore.toString(), Modifier.weight(1f), colors.second)
                }
            }
            Box(modifier = Modifier.weight(1f)) {
                IconButton(onClick = {
                    val intent = Intent(context, BoutScoreActivity::class.java)
                    intent.putExtra("bout", bout)
                    ContextCompat.startActivity(context, intent, null)
                }) {
                    Icon(Icons.Outlined.KeyboardArrowRight, "Go to bout score")
                }
            }
        }
    }
}

@Composable
private fun CardText(
    text: String,
    modifier: Modifier,
    color: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {
    Text(
        text,
        fontSize = MaterialTheme.typography.bodyLarge.fontSize * 1.2f,
        modifier = modifier,
        color = color
    )
}
