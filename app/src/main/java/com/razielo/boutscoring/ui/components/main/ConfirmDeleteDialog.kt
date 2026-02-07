package com.razielo.boutscoring.ui.components.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.razielo.boutscoring.R
import com.razielo.boutscoring.ui.models.ParsedBout

/**
 * Dialog that ask for confirmation to delete a bout
 */
@Composable
fun ConfirmDeleteDialog(
    bout: ParsedBout,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
) {
    val redCorner = bout.redCorner.fullName
    val blueCorner = bout.blueCorner.fullName
    val questionText = stringResource(
        R.string.confirm_bout_deletion,
        redCorner, blueCorner
    )

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    questionText,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.W500,
                    fontSize = 20.sp,
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TextButton(onDismissRequest) { Text(stringResource(R.string.cancel)) }
                    TextButton(onConfirmation) { Text(stringResource(R.string.confirm)) }
                }
            }
        }
    }
}

/**
 * Preview for delete confirmation dialog
 */
@Preview(showBackground = true)
@Composable
private fun ConfirmDeleteDialogPreview() {
    ConfirmDeleteDialog(bout = ParsedBout.example(), onDismissRequest = {}, onConfirmation = {})
}