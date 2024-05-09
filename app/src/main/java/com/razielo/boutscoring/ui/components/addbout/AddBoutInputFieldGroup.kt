package com.razielo.boutscoring.ui.components.addbout

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.razielo.boutscoring.R

@Composable
fun AddBoutInputFieldGroup(
    values: List<String>, corner: String, onValueChange: (Int, String) -> Unit
) {
    val labels: List<String> = listOf(
        stringResource(R.string.corner_full_name, corner),
        stringResource(R.string.corner_display_name, corner)
    )

    repeat(2) { index ->
        InputField(value = values[index],
            labelText = labels[index],
            onValueChange = { onValueChange(index, it) })
    }
}

@Composable
private fun InputField(
    value: String, labelText: String, onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        label = { Text(labelText) },
        modifier = Modifier.fillMaxWidth()
    )
}
