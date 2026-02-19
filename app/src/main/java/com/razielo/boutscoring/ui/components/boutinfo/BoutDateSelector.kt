package com.razielo.boutscoring.ui.components.boutinfo

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.razielo.boutscoring.DateUtils
import com.razielo.boutscoring.R
import com.razielo.boutscoring.data.models.BoutInfo
import com.razielo.boutscoring.ui.models.ParsedBout
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun BoutDateSelector(info: BoutInfo, setDate: (String?) -> Unit) {
    var showDialog by remember { mutableStateOf(false) }
    var displayDate: String? by remember {
        mutableStateOf(
            DateUtils.changeFormatter(
                info.date, DateUtils.isoFormatter, DateUtils.verboseFormatter
            )
        )
    }
    var calendarGrid by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Button(
            onClick = { showDialog = true },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Icon(Icons.Default.DateRange, contentDescription = null)
                Text(
                    displayDate ?: stringResource(R.string.choose_date),
                    textAlign = TextAlign.Center
                )
            }
        }

        if (showDialog) {
            BoutDatePickerDialog(
                calendarGrid = calendarGrid,
                onSwitchView = { calendarGrid = !calendarGrid },
                onDismiss = { showDialog = false },
                onConfirm = {
                    displayDate = DateUtils.dateToString(it, DateUtils.verboseFormatter)
                    setDate(DateUtils.dateToString(it, DateUtils.isoFormatter))
                    showDialog = false
                },
                initialDateStr = displayDate
            )
        }
    }
}

@Composable
private fun BoutDatePickerDialog(
    calendarGrid: Boolean,
    onSwitchView: () -> Unit,
    onDismiss: () -> Unit,
    onConfirm: (LocalDate) -> Unit,
    initialDateStr: String?
) {
    val initialDate =
        if (initialDateStr != null) {
            val millis = DateUtils.stringToMillis(initialDateStr, DateUtils.verboseFormatter)
            if (millis != null) {
                DateUtils.millisToLocalDate(millis)
            } else {
                LocalDate.now()
            }
        } else {
            LocalDate.now()
        }

    var selectedDate by remember { mutableStateOf(initialDate) }
    var currentMonth by remember { mutableStateOf(YearMonth.from(initialDate)) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                // Title
                Text(
                    stringResource(R.string.select_fight_date),
                    Modifier
                        .padding(bottom = 24.dp)
                        .fillMaxWidth(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface
                )

                AnimatedContent(
                    targetState = calendarGrid,
                    label = "CalendarTransition"
                ) { calendar ->
                    Column {
                        // Month/Year header with navigation
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (calendar) {
                                IconButton(onClick = {
                                    currentMonth = currentMonth.minusMonths(1)
                                }) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
                                        contentDescription = stringResource(R.string.previous_month)
                                    )
                                }
                            } else {
                                Spacer(Modifier)
                            }

                            Button(
                                onClick = onSwitchView,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            ) {
                                Text(
                                    text = "${
                                        currentMonth.month.getDisplayName(
                                            TextStyle.FULL,
                                            Locale.getDefault()
                                        ).capitalize(androidx.compose.ui.text.intl.Locale.current)
                                    } ${currentMonth.year}",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                )
                            }

                            if (calendar) {
                                IconButton(onClick = {
                                    currentMonth = currentMonth.plusMonths(1)
                                }) {

                                    Icon(
                                        imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                                        contentDescription = stringResource(R.string.next_month)
                                    )
                                }
                            } else {
                                Spacer(Modifier)
                            }
                        }

                        if (calendar) {
                            CalendarGrid(
                                yearMonth = currentMonth,
                                selectedDate = selectedDate,
                                onDateSelected = { selectedDate = it }
                            )
                        } else {
                            ManualDate(
                                selectedDate = selectedDate,
                                onDateSelected = { selectedDate = it }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier
                            .height(48.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                    ) {
                        Text(
                            stringResource(R.string.cancel),
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }

                    Button(
                        onClick = { onConfirm(selectedDate) },
                        modifier = Modifier
                            .height(48.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(stringResource(R.string.confirm_selection))
                    }
                }
            }
        }
    }
}

@Composable
private fun ManualDate(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    var selectedDt by remember { mutableStateOf(selectedDate) }

    val minYear = 1900
    val maxYear = LocalDate.now().year + 1

    val days = (1..selectedDt.month.length(selectedDt.isLeapYear)).toList()
    var day by remember { mutableIntStateOf(selectedDt.dayOfMonth) }

    val months = Month.entries.map { it.toString().uppercase().take(3) }
    var month by remember { mutableIntStateOf(selectedDt.monthValue) }

    val years = (minYear..maxYear).toList()
    var year by remember { mutableIntStateOf(selectedDt.year) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        DateSelectorComponent(
            options = days,
            selectedIndex = day - 1,
            onNext = {
                if (day == days.last()) day = 1 else day++
                selectedDt = selectedDt.withDayOfMonth(day)
                onDateSelected(selectedDt)
            },
            onPrevious = {
                if (day == 1) day = days.last() else day--
                selectedDt = selectedDt.withDayOfMonth(day)
                onDateSelected(selectedDt)
            }
        )

        DateSelectorComponent(
            options = months,
            selectedIndex = month - 1,
            onNext = {
                if (month == months.size) month = 1 else month++
                selectedDt = selectedDt.withMonth(month)
                onDateSelected(selectedDt)
            },
            onPrevious = {
                if (month == 1) month = months.size else month--
                selectedDt = selectedDt.withMonth(month)
                onDateSelected(selectedDt)
            }
        )

        DateSelectorComponent(
            options = years,
            selectedIndex = year - minYear,
            onNext = {
                if (year == years.last()) year = minYear else year++
                selectedDt = selectedDt.withYear(year)
                onDateSelected(selectedDt)
            },
            onPrevious = {
                if (year == minYear) year = maxYear else year--
                selectedDt = selectedDt.withYear(year)
                onDateSelected(selectedDt)
            }
        )
    }
}

@Composable
private fun DateSelectorComponent(
    options: List<Any>,
    selectedIndex: Int,
    onNext: () -> Unit,
    onPrevious: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(onClick = onPrevious) {
            Icon(
                Icons.AutoMirrored.Default.KeyboardArrowLeft,
                contentDescription = stringResource(R.string.previous),
                modifier = Modifier.rotate(90f)
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            val prevIndex = if (selectedIndex == 0) options.size - 1 else selectedIndex - 1
            val nextIndex = if (selectedIndex == options.size - 1) 0 else selectedIndex + 1

            Text(
                options[prevIndex].toString(),
                fontWeight = FontWeight.Black,
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
            )
            Text(
                options[selectedIndex].toString(),
                fontWeight = FontWeight.Black,
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                options[nextIndex].toString(),
                fontWeight = FontWeight.Black,
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
            )
        }
        IconButton(onClick = onNext) {
            Icon(
                Icons.AutoMirrored.Default.KeyboardArrowLeft,
                contentDescription = "Previous",
                modifier = Modifier.rotate(-90f)
            )
        }
    }
}

@Composable
private fun CalendarGrid(
    yearMonth: YearMonth,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    val firstDayOfMonth = yearMonth.atDay(1)
    val firstDayOfWeek = (firstDayOfMonth.dayOfWeek.value - 1) % 7
    val daysInMonth = yearMonth.lengthOfMonth()

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            DayOfWeek.entries.forEach { day ->
                val dayOfWeek = day.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                    .uppercase()
                    .first()
                    .toString()
                Text(
                    text = dayOfWeek,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    color = Color.Gray,
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        var dayCounter = 1
        repeat(6) { week ->
            if (dayCounter <= daysInMonth) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    repeat(7) { dayOfWeek ->
                        val dayNumber = if (week == 0 && dayOfWeek < firstDayOfWeek) {
                            null
                        } else if (dayCounter <= daysInMonth) {
                            dayCounter++
                        } else {
                            null
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .padding(4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            dayNumber?.let { day ->
                                val date = yearMonth.atDay(day)
                                val isSelected = date == selectedDate

                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(
                                            if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                                        )
                                        .clickable { onDateSelected(date) },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = day.toString(),
                                        color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
                                        fontSize = MaterialTheme.typography.bodySmall.fontSize,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun BoutDatePickerCalendarPreview() {
    BoutDatePickerDialog(
        onDismiss = {},
        onConfirm = {},
        initialDateStr = "2026-01-01",
        calendarGrid = true,
        onSwitchView = {}
    )
}

@Preview
@Composable
private fun BoutDatePickerManualPreview() {
    BoutDatePickerDialog(
        onDismiss = {},
        onConfirm = {},
        initialDateStr = "2026-01-01",
        calendarGrid = false,
        onSwitchView = {}
    )
}

@Preview
@Composable
private fun BoutDateSelectorPreview() {
    BoutDateSelector(ParsedBout.example().info.copy()) {}
}

