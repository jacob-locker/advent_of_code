package day1

import getInputLines

fun solveDay1(input: String, solver: (List<Int>) -> Int) = input.getInputLines().map { it.toInt() }.let(solver)

fun sweepList(input: String) = solveDay1(input) { levels ->
    levels.foldIndexed(0) { index, acc, currentValue ->
        acc + if (index > 0 && currentValue > levels[index - 1]) {
            1
        } else {
            0
        }
    }
}

fun sweepListSlidingWindow(input: String) = solveDay1(input) { levels ->
    var count = 0
    var previousSum : Int? = null
    levels.windowed(3) {
        val sum = it.sum()
        if (previousSum != null && previousSum!! < sum) {
            count++
        }
        previousSum = sum
    }

    count
}
