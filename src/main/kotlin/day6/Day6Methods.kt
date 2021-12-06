package day6

fun solveDay6(input: String, solver: (List<Int>) -> Long) = input.split(",").map { it.toInt() }.let(solver)

fun getNumberOfLanternFish(input: String, numberOfDays: Int = 80) = solveDay6(input) { internalTimers ->
    var numberOfFish = LongArray(9) { 0L }
    internalTimers.forEach { numberOfFish[it] = numberOfFish[it] + 1L }

    for (day in 0 until numberOfDays) {
        val shiftedArray = LongArray(9) { 0L }
        for (i in 1 until numberOfFish.size) {
            shiftedArray[i - 1] = numberOfFish[i]
        }
        shiftedArray[6] = shiftedArray[6] + numberOfFish[0]
        shiftedArray[8] = numberOfFish[0]

        numberOfFish = shiftedArray
    }

    numberOfFish.sum()
}
