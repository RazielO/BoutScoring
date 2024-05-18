package com.razielo.boutscoring.ui.components.boutinfo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.razielo.boutscoring.R
import com.razielo.boutscoring.data.models.BoutInfo
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun BoutDateSelector(info: BoutInfo, updateInfo: (BoutInfo) -> Unit) {
    var showDialog by remember { mutableStateOf(false) }
    var displayDate: String? by remember {
        mutableStateOf(
            DateUtils.changeFormatter(
                info.date, DateUtils.isoFormatter, DateUtils.verboseFormatter
            )
        )
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Button(onClick = { showDialog = true }, Modifier.fillMaxWidth()) {
            Text(displayDate ?: stringResource(R.string.choose_date), textAlign = TextAlign.Center)
        }
        if (showDialog) {
            DialogDatePicker(initialDate = info.date,
                dismiss = { showDialog = false },
                updateDateString = { displayDate = it },
                save = { updateInfo(info.copy(date = it)) })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DialogDatePicker(
    initialDate: String?,
    dismiss: () -> Unit,
    updateDateString: (String) -> Unit,
    save: (String?) -> Unit
) {
    val dateState = rememberDatePickerState(
        initialSelectedDateMillis = DateUtils.stringToMillis(
            initialDate, DateUtils.isoFormatter
        )
    )

    val selectedLocalDate = dateState.selectedDateMillis?.let {
        DateUtils.millisToLocalDate(it)
    }

    selectedLocalDate?.let {
        updateDateString(
            stringResource(
                R.string.date_button_label,
                DateUtils.dateToString(selectedLocalDate)
            )
        )
    }

    DatePickerDialog(onDismissRequest = dismiss, confirmButton = {
        Button(onClick = {
            dismiss()
            val newDate = selectedLocalDate?.let {
                DateUtils.dateToString(
                    selectedLocalDate, DateUtils.isoFormatter
                )
            }
            save(newDate)
        }) {
            Text(text = stringResource(R.string.ok))
        }
    }, dismissButton = {
        Button(onClick = {
            dismiss()
            dateState.setSelection(
                DateUtils.stringToMillis(
                    initialDate, DateUtils.isoFormatter
                ) ?: dateState.selectedDateMillis
            )
        }) {
            Text(text = stringResource(R.string.cancel))
        }
    }) {
        DatePicker(
            state = dateState, showModeToggle = true
        )
    }
}

private class DateUtils {
    companion object {
        var verboseFormatter: DateTimeFormatter =
            DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.getDefault())
        var isoFormatter: DateTimeFormatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())

        fun millisToLocalDate(millis: Long): LocalDate {
            return Instant.ofEpochMilli(millis).atOffset(ZoneOffset.UTC).toLocalDate()
        }

        fun stringToMillis(string: String?, formatter: DateTimeFormatter): Long? {
            return try {
                LocalDate.parse(string, formatter)
                    .atStartOfDay(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli()
            } catch (e: Exception) {
                null
            }
        }

        fun changeFormatter(
            string: String?, from: DateTimeFormatter, to: DateTimeFormatter
        ): String? {
            val millis: Long? = stringToMillis(string, from)
            val date: LocalDate? = millis?.let { millisToLocalDate(it) }
            return date?.let { dateToString(it, to) }
        }

        fun millisToLocalDateWithFormatter(
            date: LocalDate, dateTimeFormatter: DateTimeFormatter
        ): LocalDate {
            val dateInMillis = LocalDate.parse(date.format(dateTimeFormatter), dateTimeFormatter)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()

            return Instant.ofEpochMilli(dateInMillis).atZone(ZoneId.systemDefault()).toLocalDate()
        }

        fun dateToString(
            date: LocalDate, dateFormatter: DateTimeFormatter = verboseFormatter
        ): String {
            val dateInMillis = millisToLocalDateWithFormatter(date, dateFormatter)
            return dateFormatter.format(dateInMillis)
        }
    }
}
