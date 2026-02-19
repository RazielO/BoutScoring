package com.razielo.boutscoring.ui.components.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CardStyleTextField(
    labelText: String? = null,
    hintText: String,
    value: String,
    modifier: Modifier,
    onValueChange: (String) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        if (labelText != null) {
            Text(
                text = labelText,
                fontSize = MaterialTheme.typography.labelLarge.fontSize,
                fontWeight = FontWeight.SemiBold,
                color = Color.Gray,
            )
        }

        OutlinedCard(
            modifier = modifier
                .fillMaxWidth()
                .clickable(enabled = true) {},
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                enabled = true,
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                ),
                decorationBox = { innerTextField ->
                    if (value.isEmpty()) {
                        Text(
                            text = hintText,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        )
                    }
                    innerTextField()
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CardStyleTextFieldPreview() {
    CardStyleTextField(
        labelText = "Label",
        hintText = "Hint text",
        value = "Value",
        modifier = Modifier
    ) { }
}