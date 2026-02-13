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
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.razielo.boutscoring.R
import com.razielo.boutscoring.data.models.enums.WeightClass
import com.razielo.boutscoring.data.models.enums.Winner
import com.razielo.boutscoring.ui.models.ParsedBout
import kotlinx.coroutines.delay

@Composable
fun DropdownSelection(
    title: String,
    options: List<String>,
    enabled: Boolean = true,
    selectedElement: String?,
    onSelectionChanged: (String?) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf(selectedElement) }

    LaunchedEffect(selectedElement) {
        selected = selectedElement
    }

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(),
            fontSize = MaterialTheme.typography.labelLarge.fontSize,
            fontWeight = FontWeight.SemiBold,
            color = Color.Gray
        )
        DropdownSelector(
            selectedElement = selected,
            enabled = enabled,
            onClick = { showDialog = true },
            labelText = stringResource(R.string.select, title),
            modifier = Modifier.fillMaxWidth()
        )
    }

    if (showDialog) {
        DialogSelector(
            title = title,
            options = options,
            selected = selected,
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
    labelText: String,
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
        DropdownLabel(selectedElement ?: labelText)
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
            fontSize = MaterialTheme.typography.bodyMedium.fontSize
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
            Text(text)
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
    selected: String?,
    onDismiss: () -> Unit,
    onConfirm: (String?) -> Unit
) {
    var selectedElement by remember { mutableStateOf(selected) }
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
                    stringResource(R.string.select, title),
                    Modifier
                        .padding(bottom = 24.dp)
                        .fillMaxWidth(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface
                )
//                LazyColumn(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .heightIn(max = 400.dp)
//                ) {
//                    items(options.size) { idx ->
//                        val text = options[idx]
//                        DialogSelectionItem(
//                            text,
//                            text == selectedElement
//                        ) { selectedElement = text }
//                    }
//                }
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
                                text == selectedElement
                            ) { selectedElement = text }
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
                        onClick = { onConfirm(selectedElement) },
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
            val firstVisibleItemIndex = listState.firstVisibleItemIndex
            val totalItemsCount = listState.layoutInfo.totalItemsCount
            val visibleItemsCount = listState.layoutInfo.visibleItemsInfo.size

            if (totalItemsCount > 0) {
                val thumbHeight = (visibleItemsCount.toFloat() / totalItemsCount) * 1f
                val thumbOffset = (firstVisibleItemIndex.toFloat() / totalItemsCount) * 1f

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
    DropdownSelection(
        title = stringResource(R.string.winner_dropdown_label),
        options = Winner.entries.map { it.displayName },
        selectedElement = ParsedBout.example().info.winner?.displayName,
        onSelectionChanged = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun DialogSelectorPreview() {
    DialogSelector(
        title = "Weight Class",
        options = WeightClass.entries.map { it.displayName },
        selected = WeightClass.JR_FLY.displayName,
        onDismiss = {},
        onConfirm = {}
    )
}
