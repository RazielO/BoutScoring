package com.razielo.boutscoring.ui.components.addbout

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBoutTopBar(onClick: () -> Unit) {
    val colors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        titleContentColor = MaterialTheme.colorScheme.primary,
    )

    TopAppBar(colors = colors, title = {
        Text("Add new bout")
    }, navigationIcon = {
        IconButton(onClick = onClick) {
            Icon(
                imageVector = Icons.Filled.ArrowBack, contentDescription = "Go Back"
            )
        }
    })
}
