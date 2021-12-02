val DAY_1_SONAR_SWEEP_TEST_INPUT = listOf<Int>(2, 5, 3, 2, 5)

fun sweepList(input: String) = sweepList(parseDay1Input(input))

fun sweepList(levels: List<Int>) =
    levels.foldIndexed(0) { index, acc, currentValue ->
        acc + if (index > 0 && currentValue > levels[index - 1]) {
            1
        } else {
            0
        }
    }

fun sweepListSlidingWindow(input: String) = sweepListSlidingWindow(parseDay1Input(input))

fun sweepListSlidingWindow(levels: List<Int>) : Int {
    var count = 0
    var previousSum : Int? = null
    levels.windowed(3) {
        val sum = it.sum()
        if (previousSum != null && previousSum!! < sum) {
            count++
        }
        previousSum = sum
    }

    return count
}

fun parseDay1Input(input: String) : List<Int> {
    val lines = input.getInputLines()

    return lines.map { it.toInt() }
}
