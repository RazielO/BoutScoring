package com.razielo.boutscoring.ui.components.addbout

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.razielo.boutscoring.R
import com.razielo.boutscoring.ui.components.common.CardStyleTextField
import com.razielo.boutscoring.ui.theme.BoutScoringTheme
import com.razielo.boutscoring.ui.theme.redContainerDark

@Composable
fun AddBoutInputFieldGroup(
    values: List<String>,
    label: String,
    color: Color,
    onValueChange: (Int, String) -> Unit
) {
    val labels: List<String> = listOf(
        label,
        stringResource(R.string.corner_display_name)
    )
    var showDialog by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = labels[0].uppercase(),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = color,
            letterSpacing = 1.sp
        )

        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = true) {},
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.outlinedCardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
            border = BorderStroke(1.dp, color)
        ) {
            BasicTextField(
                value = values[0],
                onValueChange = { onValueChange(0, it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                enabled = true,
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
                decorationBox = { innerTextField ->
                    if (values[0].isEmpty()) {
                        Text(
                            text = stringResource(R.string.enter_name),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    innerTextField()
                }
            )
        }

        if (values[0].isNotEmpty()) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Text(
                    stringResource(R.string.will_be_displayed_as, values[1]),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
                TextButton(onClick = { showDialog = true }) {
                    Text(stringResource(R.string.edit))
                }
            }
        }
    }

    if (showDialog) {
        EditDisplayDialog(
            values[0],
            values[1],
            onDismiss = { showDialog = false },
            onConfirm = {
                onValueChange(1, it)
                showDialog = false
            }
        )
    }
}

@Composable
private fun EditDisplayDialog(
    fighterName: String,
    initialValue: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var value by remember { mutableStateOf(initialValue) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Title
                Text(
                    stringResource(R.string.edit_display_name, fighterName),
                    Modifier
                        .padding(bottom = 24.dp)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface
                )

                CardStyleTextField(
                    hintText = stringResource(R.string.corner_display_name),
                    value = value,
                    singleLine = true,
                    modifier = Modifier
                ) {
                    value = it
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier
                            .height(48.dp)
                            .weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                    ) {
                        Text(
                            stringResource(R.string.cancel),
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }

                    Button(
                        onClick = { onConfirm(value) },
                        modifier = Modifier
                            .height(48.dp)
                            .weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(stringResource(R.string.save))
                    }
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun AddBoutInputFieldGroupPreview() {
    BoutScoringTheme {
        AddBoutInputFieldGroup(
            values = listOf("Okesandr Usyk", "USYK"),
            label = stringResource(R.string.red_corner_full_name),
            color = redContainerDark,
            onValueChange = { _, _ -> })
    }
}

@Preview(showBackground = true)
@Composable
private fun EditDisplayDialogPreview() {
    BoutScoringTheme {
        EditDisplayDialog(
            fighterName = "Oleksandr Usyk",
            initialValue = "USYK",
            onDismiss = {},
            onConfirm = {}
        )
    }
}