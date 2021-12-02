import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    executeDay(1, "Sonar Scan") {
        method { sweepList(it) }
        method { sweepListSlidingWindow(it) }
    }

    executeDay(2, "Dive!") {
        method { dive(it) }
        method { diveWithAim(it) }
    }
}

class DayScope {
    val parts : List<((String) -> Any)>
    get() = _parts

    private val _parts = mutableListOf<((String) -> Any)>()

    fun method(partMethod: ((String) -> Any)) {
        _parts.add(partMethod)
    }
}

suspend fun executeDay(dayNumber: Int, dayTitle: String, dayScopeAction: DayScope.() -> Unit) {
    println("-- Day $dayNumber: $dayTitle --")
    val dayInput = retrieveInput(dayNumber)

    println("Input: ${dayInput.getInputLines()}")

    val dayScope = DayScope()
    dayScopeAction.invoke(dayScope)

    dayScope.parts.forEachIndexed { index, partMethod ->
        println("Part ${index + 1} Output: ${partMethod(dayInput)}" )
    }
    println()
}