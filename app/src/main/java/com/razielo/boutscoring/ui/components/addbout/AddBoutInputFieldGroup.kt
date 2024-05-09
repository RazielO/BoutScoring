package com.razielo.boutscoring.ui.components.addbout

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction

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
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done
        ),
        label = { Text(labelText) }, modifier = Modifier.fillMaxWidth(),
    )
}
