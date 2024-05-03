package com.razielo.boutscoring

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.razielo.boutscoring.data.models.Bout
import com.razielo.boutscoring.data.models.DrawMethod
import com.razielo.boutscoring.data.models.Fighter
import com.razielo.boutscoring.data.models.NoResultMethod
import com.razielo.boutscoring.data.models.WinMethod
import com.razielo.boutscoring.data.models.Winner
import com.razielo.boutscoring.ui.components.main.MainComponent
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bouts: List<Bout> = (1 .. 100).map { generateRandomBout() }

        setContent {
            MainComponent(context = this, bouts)
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

