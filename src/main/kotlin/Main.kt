import day1.sweepList
import day1.sweepListSlidingWindow
import day2.dive
import day2.diveWithAim
import day3.lifeSupportRating
import day3.powerConsumption
import day4.findLastWinningScore
import day4.findWinningScore
import day5.findNumberOfPointsWithLineOverlap
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
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