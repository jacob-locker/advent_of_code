package day7

import kotlin.math.abs

fun solveDay7(input: String, solver: (List<Int>) -> Int) = input.split(",").map { it.toInt() }.let(solver)

fun minFuelCostToAlignCrabs(input: String, heuristic: (Int) -> Int = { it }) = solveDay7(input) { horizontalPositions ->
    // [30, 40, 23, 5, 100, 25, 75]
    // [5, 6, 7, 8, 9, 10, ... 100]
    val min = horizontalPositions.minOf { it }
    val max = horizontalPositions.maxOf { it }

    IntArray(max - min + 1){ min + it}.minOf { getFuelCost(horizontalPositions, it, heuristic) }
}

internal fun getFuelCost(crabPositions: List<Int>, alignPosition: Int, heuristic: (Int) -> Int) = crabPositions.sumOf { heuristic(abs(alignPosition - it)) }

object Heuristic {
    private val costMemo = mutableMapOf<Int, Int>()
    val CrabFuelCost: (Int) -> Int = ::fuelCost

    private fun fuelCost(n: Int): Int {
        if (costMemo.containsKey(n)) {
            return costMemo[n]!!
        }

        if (n <= 0) {
            return 0
        }

        costMemo[n] = fuelCost(n - 1) + n
        return costMemo[n]!!
    }
}
