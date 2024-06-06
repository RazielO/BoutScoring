package com.razielo.boutscoring.ui.components.addbout

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import com.razielo.boutscoring.R

@Composable
fun AddBoutInputFieldGroup(
    values: List<String>, corner: String, onValueChange: (Int, String) -> Unit
) {
    val labels: List<String> = listOf(
        stringResource(R.string.corner_full_name, corner),
        stringResource(R.string.corner_display_name, corner)
    )

    InputField(
        value = values[0],
        labelText = labels[0],
        onValueChange = { onValueChange(0, it) },
        capitalization = KeyboardCapitalization.Words
    )

    InputField(
        value = values[1],
        labelText = labels[1],
        onValueChange = { onValueChange(1, it) },
        capitalization = KeyboardCapitalization.Characters
    )
}

@Composable
private fun InputField(
    value: String,
    labelText: String,
    onValueChange: (String) -> Unit,
    capitalization: KeyboardCapitalization
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done,
            capitalization = capitalization,
            keyboardType = KeyboardType.Text
        ),
        label = { Text(labelText) },
        modifier = Modifier.fillMaxWidth()
    )
}
