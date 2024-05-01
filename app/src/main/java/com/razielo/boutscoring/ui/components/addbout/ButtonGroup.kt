package com.razielo.boutscoring.ui.components.addbout

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Composable
fun ButtonGroup(values: List<Int>, onClick: (Int) -> Unit, selectedIndex: Int) {
    Row(modifier = Modifier.padding(bottom = 16.dp)) {
        values.forEachIndexed { index, value ->
            RoundButton(
                shape = when (index) {
                    0               -> RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp)
                    values.size - 1 -> RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp)
                    else            -> RectangleShape
                },
                modifier = Modifier.weight(1f),
                enabled = selectedIndex != index,
                value = value.toString(),
            ) { onClick(index) }
        }
    }
}

@Composable
private fun RoundButton(
    shape: Shape,
    modifier: Modifier,
    enabled: Boolean,
    value: String,
    onClick: () -> Unit,
) {
    OutlinedButton(
        shape = shape, onClick = onClick, colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
            disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            disabledContentColor = MaterialTheme.colorScheme.onSecondaryContainer
        ), modifier = modifier, contentPadding = PaddingValues(0.dp), enabled = enabled
    ) {
        Text(value)
    }
}
