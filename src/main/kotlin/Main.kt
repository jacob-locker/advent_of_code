import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import day1.sweepList
import day1.sweepListSlidingWindow
import day2.dive
import day2.diveWithAim
import day3.lifeSupportRating
import day3.powerConsumption
import day4.findLastWinningScore
import day4.findWinningScore
import day5.findNumberOfPointsWithLineOverlap
import day6.getNumberOfLanternFish
import day7.Heuristic
import day7.minFuelCostToAlignCrabs
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.YearMonth

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
        printDay(1, "Sonar Scan") {
            printPart { sweepList(it) }
            printPart { sweepListSlidingWindow(it) }
        }

        printDay(2, "Dive!") {
            printPart { dive(it) }
            printPart { diveWithAim(it) }
        }

        printDay(3, "Binary Diagnostic") {
            printPart { powerConsumption(it) }
            printPart { lifeSupportRating(it) }
        }

        printDay(4, "Giant Squid") {
            printPart { findWinningScore(it) }
            printPart { findLastWinningScore(it) }
        }

        printDay(5, "Hydrothermal Venture") {
            printPart { findNumberOfPointsWithLineOverlap(it) }
            printPart { findNumberOfPointsWithLineOverlap(it, ignoresDiagonals = false) }
        }

        printDay(6, "Lanternfish") {
            printPart { getNumberOfLanternFish(it) }
            printPart { getNumberOfLanternFish(it, numberOfDays = 256) }
        }

        printDay(7, "The Treachery of Whales") {
            printPart { minFuelCostToAlignCrabs(it) }
            printPart { minFuelCostToAlignCrabs(it, heuristic = Heuristic.CrabFuelCost) }
        }
    }
}

@Composable
@Preview
fun App() {
    var text = remember { mutableStateOf("Hello, World!") }

    MaterialTheme {
//        Button(onClick = {
//            text.value = "Hello, Desktop!"
//        }) {
//            Text(text.value)
//        }
    }
}

class DayScope {
    val parts : List<((String) -> Any)>
    get() = _parts

    private val _parts = mutableListOf<((String) -> Any)>()

    fun printPart(partMethod: ((String) -> Any)) {
        _parts.add(partMethod)
    }
}

fun printDay(dayNumber: Int, dayTitle: String, dayScopeAction: DayScope.() -> Unit) {
    println("-- Day $dayNumber: $dayTitle --")
    val dayInput = InputRetriever().retrieveInput(dayNumber)

    println("Input: ${dayInput.getInputLines()}")

    val dayScope = DayScope()
    dayScopeAction.invoke(dayScope)

    dayScope.parts.forEachIndexed { index, partMethod ->
        println("Part ${index + 1} Output: ${partMethod(dayInput)}" )
    }
    println()
}