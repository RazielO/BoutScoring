package com.razielo.boutscoring.ui.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * Generic pill element
 */
@Composable
fun Pill(
    text: String,
    modifier: Modifier = Modifier,
    background: Color,
    textColor: Color = Color.Black
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clip(RoundedCornerShape(48.dp))
            .background(background)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text,
            style = MaterialTheme.typography.labelLarge.copy(color = textColor),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
    }
}
