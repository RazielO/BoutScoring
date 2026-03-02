package com.razielo.boutscoring.ui.components.boutinfo

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.razielo.boutscoring.R
import com.razielo.boutscoring.data.models.enums.WeightClass
import com.razielo.boutscoring.data.models.enums.WeightClass.Companion.displayName
import com.razielo.boutscoring.data.models.enums.Winner
import com.razielo.boutscoring.ui.theme.BoutScoringTheme
import kotlinx.coroutines.delay

@Composable
fun DropdownSelection(
    label: String,
    hintText: String,
    options: List<String>,
    enabled: Boolean = true,
    selectedIndex: Int?,
    onSelectionChanged: (Int?) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf(selectedIndex) }

    LaunchedEffect(selectedIndex) {
        selected = selectedIndex
    }

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            color = Color.Gray
        )
        DropdownSelector(
            selectedElement = selected?.let { options[it] },
            enabled = enabled,
            onClick = { showDialog = true },
            hintText = hintText,
            modifier = Modifier.fillMaxWidth()
        )
    }

    if (showDialog) {
        DialogSelector(
            title = label,
            options = options,
            selectedIndex = selected,
            onDismiss = { showDialog = false },
            onConfirm = {
                selected = it
                onSelectionChanged(it)
                showDialog = false
            }
        )
    }
}


@Composable
private fun DropdownSelector(
    selectedElement: String?,
    onClick: () -> Unit,
    enabled: Boolean,
    hintText: String,
    modifier: Modifier = Modifier
) {
    val disabledColors =
        CardDefaults.outlinedCardColors(
            contentColor = Color.LightGray,
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    val colors =
        if (enabled) CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant) else disabledColors

    OutlinedCard(
        modifier = modifier.clickable(enabled = enabled, onClick = onClick),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = colors
    ) {
        DropdownLabel(selectedElement ?: hintText)
    }
}

@Composable
private fun DropdownLabel(text: String) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Text(
            text,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth(),
            style = MaterialTheme.typography.bodyMedium
        )
        Icon(
            Icons.Outlined.KeyboardArrowDown,
            null,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
private fun DialogSelectionItem(text: String, selected: Boolean, onClick: () -> Unit) {
    val color =
        if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.5f) else Color.Transparent

    TextButton(
        modifier = Modifier
            .fillMaxWidth(),
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            contentColor = MaterialTheme.colorScheme.onSurface,
            containerColor = color
        ),
        contentPadding = PaddingValues(all = 16.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text,
                style = MaterialTheme.typography.bodyMedium
            )
            Icon(
                Icons.Filled.CheckCircle,
                contentDescription = null,
                tint = if (selected) MaterialTheme.colorScheme.onPrimary else Color.Transparent
            )
        }
    }
}

@Composable
private fun DialogSelector(
    title: String,
    options: List<String>,
    selectedIndex: Int?,
    onDismiss: () -> Unit,
    onConfirm: (Int?) -> Unit
) {
    var selected by remember { mutableStateOf(selectedIndex) }
    val listState = rememberLazyListState()

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(vertical = 24.dp, horizontal = 8.dp)
                    .wrapContentSize()
            ) {
                // Title
                Text(
                    title,
                    Modifier
                        .padding(bottom = 24.dp)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp)
                ) {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(options.size) { idx ->
                            val text = options[idx]
                            DialogSelectionItem(
                                text,
                                selected != null && options[selected!!] == text
                            ) { selected = idx }
                        }
                    }

                    // Scrollbar
                    if (options.size > 5) { // Only show if there are many items
                        VerticalScrollbar(
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .fillMaxHeight()
                                .padding(end = 4.dp),
                            listState = listState
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
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
                        onClick = { onConfirm(selected) },
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
fun VerticalScrollbar(
    modifier: Modifier = Modifier,
    listState: LazyListState
) {
    val isScrollInProgress = listState.isScrollInProgress
    val showScrollbar = remember { mutableStateOf(false) }

    LaunchedEffect(isScrollInProgress) {
        if (isScrollInProgress) {
            showScrollbar.value = true
        } else {
            delay(1000)
            showScrollbar.value = false
        }
    }

    AnimatedVisibility(
        visible = showScrollbar.value,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .width(4.dp)
                .fillMaxHeight()
                .background(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(2.dp)
                )
        ) {
            val firstVisibleItemIndex =
                remember { derivedStateOf { listState.firstVisibleItemIndex } }
            val totalItemsCount =
                remember { derivedStateOf { listState.layoutInfo } }.value.totalItemsCount
            val visibleItemsCount =
                remember { derivedStateOf { listState.layoutInfo } }.value.visibleItemsInfo.size

            if (totalItemsCount > 0) {
                val thumbHeight = (visibleItemsCount.toFloat() / totalItemsCount) * 1f
                val thumbOffset = (firstVisibleItemIndex.value.toFloat() / totalItemsCount) * 1f

                Box(
                    modifier = Modifier
                        .fillMaxHeight(thumbHeight.coerceIn(0.1f, 1f))
                        .fillMaxWidth()
                        .align(Alignment.TopStart)
                        .offset(y = with(LocalDensity.current) {
                            (thumbOffset * 400.dp.toPx()).toDp()
                        })
                        .background(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(2.dp)
                        )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DropdownSelectionPreview() {
    BoutScoringTheme {
        DropdownSelection(
            label = stringResource(R.string.result_dropdown_label),
            hintText = stringResource(R.string.select_result),
            options = Winner.entries.map { stringResource(it.displayName) },
            selectedIndex = 0,
            onSelectionChanged = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DialogSelectorPreview() {
    BoutScoringTheme {
        DialogSelector(
            title = stringResource(R.string.select_weight_class),
            options = WeightClass.entries.map { it.displayName() },
            selectedIndex = 5,
            onDismiss = {},
            onConfirm = {}
        )
    }
}
