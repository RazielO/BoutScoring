package com.razielo.boutscoring

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import com.razielo.boutscoring.data.models.Bout
import com.razielo.boutscoring.data.models.DrawMethod
import com.razielo.boutscoring.data.models.Fighter
import com.razielo.boutscoring.data.models.NoResultMethod
import com.razielo.boutscoring.data.models.WinMethod
import com.razielo.boutscoring.data.models.Winner
import com.razielo.boutscoring.ui.components.common.BoutScoreResult
import com.razielo.boutscoring.ui.components.common.TopBar
import com.razielo.boutscoring.ui.theme.BoutScoringTheme
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bouts: List<Bout> = (1 .. 100).map { generateRandomBout() }

        setContent {
            BoutScoringTheme {
                MainScaffold(this, bouts)
            }
        }
    }
}

private fun generateRandomFighter(): Fighter {
    val randomFirstNames = listOf(
        "John",
        "Robert",
        "Michael",
        "William",
        "David",
        "Richard",
        "Joseph",
        "Charles",
        "Thomas",
        "Daniel",
        "Matthew",
        "Anthony",
        "Donald",
        "Paul",
        "Mark",
        "James",
        "Steven",
        "George",
        "Kenneth",
        "Edward"
    )

    val randomLastNames = listOf(
        "Smith",
        "Johnson",
        "Williams",
        "Brown",
        "Jones",
        "Miller",
        "Davis",
        "Garcia",
        "Rodriguez",
        "Martinez",
        "Hernandez",
        "Lopez",
        "Gonzalez",
        "Wilson",
        "Anderson",
        "Thomas",
        "Taylor",
        "Moore",
        "Jackson",
        "Martin"
    )

    val random = Random.Default

    val firstName = randomFirstNames.random(random)
    val lastName = randomLastNames.random(random)

    return Fighter("$firstName $lastName", lastName.uppercase())
}

private fun generateRandomBout(): Bout {
    val random = Random.Default
    val roundList: List<Int> = listOf(3, 4, 5, 6, 8, 10, 12, 15)
    val rounds = roundList.random(random)
    val winner: Winner? =
        Winner.entries.toMutableList<Winner?>().apply { this.add(null) }.random(random)
    val winMethod: WinMethod? = when (winner) {
        null, Winner.NO_RESULT, Winner.DRAW -> null
        Winner.RED_CORNER, Winner.BLUE_CORNER -> WinMethod.entries.toMutableList<WinMethod?>()
            .apply { this.add(null) }
            .random(random)
    }
    val drawMethod: DrawMethod? = when (winner) {
        null, Winner.NO_RESULT, Winner.RED_CORNER, Winner.BLUE_CORNER -> null
        Winner.DRAW -> DrawMethod.entries.toMutableList<DrawMethod?>()
            .apply { this.add(null) }
            .random(random)
    }
    val noResultMethod: NoResultMethod? = when (winner) {
        null, Winner.DRAW, Winner.RED_CORNER, Winner.BLUE_CORNER -> null
        Winner.NO_RESULT -> NoResultMethod.entries.toMutableList<NoResultMethod?>()
            .apply { this.add(null) }
            .random(random)
    }

    return Bout(
        rounds,
        (1 .. rounds).associateWith { Pair((6 .. 10).random(random), (6 .. 10).random(random)) },
        generateRandomFighter(),
        generateRandomFighter(),
        winner,
        winMethod,
        drawMethod,
        noResultMethod
    )
}

@Composable
private fun MainScaffold(context: Context, bouts: List<Bout>) {
    val topBarText = if (bouts.isEmpty()) "My bouts" else "My ${bouts.size} bouts"
    Scaffold(topBar = {
        TopBar(titleText = topBarText, goBack = false)
    }, floatingActionButton = { FloatingButton(context) }, content = {
        Surface(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Content(context, bouts)
        }
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Content(context: Context, bouts: List<Bout>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(16.dp)
    ) {
        items(bouts.size) { index ->
            Card(modifier = Modifier.fillMaxWidth(), onClick = {
                val intent = Intent(context, BoutScoreActivity::class.java)
                intent.putExtra("bout", bouts[index])
                startActivity(context, intent, null)
            }, enabled = false) {
                val redScore = bouts[index].scores.values.sumOf { it.first }
                val blueScore = bouts[index].scores.values.sumOf { it.second }
                val colors = scoreColors(
                    Pair(redScore, blueScore),
                    MaterialTheme.colorScheme.onSurfaceVariant
                )

                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier.weight(7f)) {
                        Row {
                            CardText(bouts[index].redCorner.fullName, Modifier.weight(5f))
                            CardText(redScore.toString(), Modifier.weight(1f), colors.first)
                        }
                        Row {
                            CardText("vs", Modifier.weight(3f))
                            BoutScoreResult(bouts[index], Modifier.weight(1f))
                        }
                        Row {
                            CardText(bouts[index].blueCorner.fullName, Modifier.weight(5f))
                            CardText(blueScore.toString(), Modifier.weight(1f), colors.second)
                        }
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        IconButton(onClick = {
                            val intent = Intent(context, BoutScoreActivity::class.java)
                            intent.putExtra("bout", bouts[index])
                            startActivity(context, intent, null)
                        }) {
                            Icon(Icons.Outlined.KeyboardArrowRight, "Go to bout score")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CardText(
    text: String,
    modifier: Modifier,
    color: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {
    Text(
        text,
        fontSize = MaterialTheme.typography.bodyLarge.fontSize * 1.2f,
        modifier = modifier,
        color = color
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
