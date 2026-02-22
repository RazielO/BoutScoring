package com.razielo.boutscoring.ui.components.addbout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.razielo.boutscoring.ui.theme.BoutScoringTheme

@Composable
fun ButtonGroup(values: List<Int>, selectedIndex: Int, onClick: (Int) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    )
    {
        values.forEachIndexed { index, value ->
            RoundButton(
                modifier = Modifier.weight(1f),
                enabled = selectedIndex != index,
                value = value.toString(),
            ) { onClick(index) }
        }
    }
}

@Composable
private fun RoundButton(
    modifier: Modifier,
    enabled: Boolean,
    value: String,
    onClick: () -> Unit,
) {
    ElevatedCard(
        shape = RoundedCornerShape(8.dp),
        onClick = onClick,
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledContainerColor = MaterialTheme.colorScheme.primary,
            disabledContentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        modifier = modifier,
        enabled = enabled,
        elevation = CardDefaults.cardElevation(8.dp),
    ) {
        Text(
            value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (enabled) FontWeight.Normal else FontWeight.Bold,
            maxLines = 1,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ButtonGroupPreview() {
    BoutScoringTheme {
        ButtonGroup(
            values = listOf(3, 5, 6, 8, 10, 12, 15),
            selectedIndex = 1,
            onClick = {},
        )
    }
}