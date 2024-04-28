package com.razielo.boutscoring

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat.startActivity
import com.razielo.boutscoring.ui.theme.BoutScoringTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BoutScoringTheme {
                Scaffold(
                    topBar = {},
                    floatingActionButton = { FloatingButton(this) },
                    content = {
                        Surface(
                            modifier = Modifier
                                .padding(it)
                                .fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            Greeting("Android")
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!", modifier = modifier
    )
}

@Composable
fun FloatingButton(context: Context) {
    FloatingActionButton(onClick = {
        val intent = Intent(context, AddBoutActivity::class.java)
        startActivity(context, intent, null)
    }) {
        Icon(Icons.Filled.Add, "Add new bout.")
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BoutScoringTheme {
        Greeting("Android")
    }
}