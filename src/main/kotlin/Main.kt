import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val inputRetriever = InputRetriever()

    println("Day 1 Sonar Sweep: ")
    val day1Input = inputRetriever.retrieveInput(1)
    println("Part 1 Output: " + sweepList(day1Input))
    println("Part 2 Output: " + sweepListSlidingWindow(day1Input))
}