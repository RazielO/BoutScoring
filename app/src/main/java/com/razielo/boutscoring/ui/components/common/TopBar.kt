package com.razielo.boutscoring.ui.components.common

import androidx.compose.foundation.layout.RowScope
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
fun TopBar(
    titleText: String,
    goBack: Boolean,
    onBack: () -> Unit = {},
    actions: @Composable() (RowScope.() -> Unit) = {}
) {
    val colors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        titleContentColor = MaterialTheme.colorScheme.primary,
    )

    TopAppBar(
        colors = colors,
        title = { Text(titleText) },
        navigationIcon = { if (goBack) GoBackButton(onBack) },
        actions = actions
    )
}

@Composable
private fun GoBackButton(onClick: () -> Unit) {
    IconButton(onClick) {
        Icon(
            imageVector = Icons.Filled.ArrowBack, contentDescription = "Go Back"
        )
    }
}
