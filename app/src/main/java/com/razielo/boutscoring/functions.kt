package com.razielo.boutscoring

import androidx.compose.ui.graphics.Color
import com.razielo.boutscoring.data.models.Bout
import com.razielo.boutscoring.data.models.DrawMethod
import com.razielo.boutscoring.data.models.Fighter
import com.razielo.boutscoring.data.models.NoResultMethod
import com.razielo.boutscoring.data.models.Screen
import com.razielo.boutscoring.data.models.WinMethod
import com.razielo.boutscoring.data.models.Winner
import com.razielo.boutscoring.ui.theme.Green
import com.razielo.boutscoring.ui.theme.Red
import kotlin.random.Random

fun scoreColors(scores: Pair<Int, Int>, default: Color): Pair<Color, Color> = with(scores) {
    if (this.first > this.second) {
        Pair(Green, Red)
    } else if (this.second > this.first) {
        Pair(Red, Green)
    } else {
        Pair(default, default)
    }
}

fun topBarTitle(screen: Screen, boutCount: Int = 0): String {
    return when (screen) {
        Screen.MAIN -> if (boutCount == 0) "My bouts" else "My $boutCount bouts"
        Screen.FILTERED_BOUTS -> "Filtered bouts"
        Screen.ADD_BOUT -> "Add new bout"
        Screen.SCORE_BOUT -> "Score bout"
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

    return Fighter(fullName = "$firstName $lastName", displayName = lastName.uppercase())
}

fun generateRandomBout(): Bout {
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
        rounds = rounds,
        scores = (1 .. rounds).associateWith {
            Pair(
                (6 .. 10).random(random),
                (6 .. 10).random(random)
            )
        },
        redCorner = generateRandomFighter(),
        blueCorner = generateRandomFighter(),
        winner = winner,
        winMethod = winMethod,
        drawMethod = drawMethod,
        noResultMethod = noResultMethod
    )
}

