package com.razielo.boutscoring

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.razielo.boutscoring.ui.theme.BoutScoringTheme
import kotlinx.coroutines.launch

class AddBoutActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BoutScoringTheme {
                ScaffoldComponent { finish() }
            }
        }
    }
}

@Composable
fun ScaffoldComponent(topBarOnCLick: () -> Unit) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(topBar = { TopBar(topBarOnCLick) }, snackbarHost = {
        SnackbarHost(hostState = snackbarHostState)
    }, content = {
        Surface(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Content(snackbarHostState)
        }
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(onClick: () -> Unit) {
    TopAppBar(colors = topAppBarColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        titleContentColor = MaterialTheme.colorScheme.primary,
    ), title = {
        Text("Add new bout")
    }, navigationIcon = {
        IconButton(onClick = onClick) {
            Icon(
                imageVector = Icons.Filled.ArrowBack, contentDescription = "Go Back"
            )
        }
    })
}

fun updateCornerValues(values: List<String>, index: Int, newValue: String): List<String> {
    return values.toMutableList().apply {
        if (index == 1) {
            set(1, newValue.uppercase())
        } else {
            val parts: List<String> =
                newValue.uppercase().trim().split(" ").filter { it.isNotEmpty() }
            val suffixes: List<String> = listOf("JR", "SR", "II", "III")

            if (parts.last().isNotEmpty() && suffixes.contains(parts.last())) {
                set(1, parts.takeLast(2).joinToString(" "))
            } else {
                set(1, parts.last())
            }

            set(0, newValue.split(" ").joinToString(" ") {
                it.replaceFirstChar { chr -> chr.uppercase() }
            })
        }
    }
}

@Composable
fun Content(snackbarHostState: SnackbarHostState) {
    var redCornerValues by remember { mutableStateOf(List(2) { "" }) }
    var blueCornerValues by remember { mutableStateOf(List(2) { "" }) }
    var selectedButtonIndex by remember { mutableIntStateOf(-1) }
    val rounds: List<Int> = listOf(3, 4, 5, 6, 8, 10, 12, 15)

    Column(modifier = Modifier.padding(16.dp)) {
        InputFieldGroup(
            redCornerValues, "Red"
        ) { index, newValue ->
            redCornerValues = updateCornerValues(redCornerValues, index, newValue)
        }

        Text(
            text = "vs",
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            textAlign = TextAlign.Center
        )

        InputFieldGroup(
            blueCornerValues, "Blue"
        ) { index, newValue ->
            blueCornerValues = updateCornerValues(blueCornerValues, index, newValue)
        }

        Text(
            text = "Number of rounds",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            textAlign = TextAlign.Center
        )

        ButtonGroup(rounds, { index -> selectedButtonIndex = index }, selectedButtonIndex)

        ContinueButton(snackbarHostState, redCornerValues, blueCornerValues, selectedButtonIndex)
    }
}

@Composable
fun InputFieldGroup(
    values: List<String>, corner: String, onValueChange: (Int, String) -> Unit
) {
    val labels: List<String> = listOf(
        "$corner Corner Full Name", "$corner Corner Display Name"
    )

    repeat(2) { index ->
        InputField(value = values[index],
            labelText = labels[index],
            onValueChange = { newValue -> onValueChange(index, newValue) })
    }
}

@Composable
fun InputField(
    value: String, labelText: String, onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = { newValue ->
            onValueChange(newValue)
        },
        label = { Text(labelText) }, modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
fun RoundButton(
    shape: Shape,
    modifier: Modifier,
    enabled: Boolean,
    value: String,
    onClick: () -> Unit,
) {
    OutlinedButton(
        shape = shape, onClick = onClick, colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
            disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            disabledContentColor = MaterialTheme.colorScheme.onSecondaryContainer
        ), modifier = modifier, contentPadding = PaddingValues(0.dp), enabled = enabled
    ) {
        Text(value)
    }
}

@Composable
fun ButtonGroup(values: List<Int>, onClick: (Int) -> Unit, selectedIndex: Int) {
    Row(modifier = Modifier.padding(bottom = 16.dp)) {
        values.forEachIndexed { index, value ->
            RoundButton(
                shape = when (index) {
                    0 -> RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp)
                    values.size - 1 -> RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp)
                    else -> RectangleShape
                },
                modifier = Modifier.weight(1f),
                enabled = selectedIndex != index,
                value = value.toString(),
            ) { onClick(index) }
        }
    }
}

@Composable
fun ContinueButton(
    snackbarHostState: SnackbarHostState,
    redCornerValues: List<String>,
    blueCornerValues: List<String>,
    selectedButtonIndex: Int,
) {
    val scope = rememberCoroutineScope()

    Button(
        onClick = {
            if (redCornerValues.any { it.isBlank() }) {
                scope.launch {
                    snackbarHostState.showSnackbar("Fill the red corner info first")
                }
            } else if (blueCornerValues.any { it.isBlank() }) {
                scope.launch {
                    snackbarHostState.showSnackbar("Fill the blue corner info first")
                }
            } else if (selectedButtonIndex == -1) {
                scope.launch {
                    snackbarHostState.showSnackbar("Select the number of rounds first")
                }
            }
        },
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text("Continue")
    }
}