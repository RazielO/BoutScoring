package com.razielo.boutscoring.ui.components.boutinfo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DropdownSelection(
    title: String,
    options: List<String>,
    enabled: Boolean = true,
    selected: String?,
    onSelectionChanged: (String) -> Unit
) {
    Column {
        Text(
            text = title,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        DropdownSelector(
            list = options,
            preselected = selected ?: "Select $title",
            enabled = enabled,
            onSelectionChanged = onSelectionChanged,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun DropdownSelector(
    list: List<String>,
    preselected: String,
    onSelectionChanged: (String) -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    var selected by remember { mutableStateOf(preselected) }
    var expanded by remember { mutableStateOf(false) }

    val onChangeSelection: (String) -> Unit = {
        selected = it
        expanded = false
        onSelectionChanged(selected)
    }

    val disabledColors = CardDefaults.outlinedCardColors(contentColor = Color.LightGray)

    OutlinedCard(
        modifier = modifier.clickable(enabled = enabled) { expanded = !expanded },
        colors = if (enabled) CardDefaults.outlinedCardColors() else disabledColors
    ) {
        DropdownLabel(selected)
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            list.forEach {
                DropdownSelectionItem(it) { onChangeSelection(it) }
            }
        }
    }
}

@Composable
private fun DropdownLabel(text: String) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top
    ) {
        Text(
            text,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth()
        )
        Icon(Icons.Outlined.ArrowDropDown, null, modifier = Modifier.padding(8.dp))
    }
}

@Composable
private fun DropdownSelectionItem(text: String, onClick: () -> Unit) {
    DropdownMenuItem(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        text = { Text(text, modifier = Modifier.fillMaxWidth()) },
    )
}
