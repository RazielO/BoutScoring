package com.razielo.boutscoring.ui.components.addbout

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AddBoutInputFieldGroup(
    values: List<String>, corner: String, onValueChange: (Int, String) -> Unit
) {
    val labels: List<String> = listOf(
        "$corner Corner Full Name", "$corner Corner Display Name"
    )

    repeat(2) { index ->
        InputField(value = values[index],
            labelText = labels[index],
            onValueChange = { newValue -> onValueChange(index, newValue) })
    }
}

@Composable
private fun InputField(
    value: String, labelText: String, onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = { newValue ->
            onValueChange(newValue)
        },
        label = { Text(labelText) }, modifier = Modifier.fillMaxWidth(),
    )
}
