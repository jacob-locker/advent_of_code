package day15

import BaseDay
import getInputBlocks
import getInputLines
import java.util.*

class Day15 : BaseDay(15, "Chiton") {
    override suspend fun partOne(input: String) = solveDay15(input) { riskMap ->
        riskMap.findLeastRiskPath()?.risk ?: 0
    }

    override suspend fun partTwo(input: String) = solveDay15(input) { riskMap ->
        with(riskMap) {
            grow(5)
            findLeastRiskPath()?.risk ?: 0
        }
    }

    private fun solveDay15(input: String, solver: (RiskMap) -> Int) = input.getInputLines().parseRiskMap().let(solver)
}

fun List<String>.parseRiskMap() = RiskMap(map { it.map { char -> char.digitToInt() }.toIntArray() }.toTypedArray())

data class RiskState(val risk: Int, val row: Int, val col: Int) : Comparable<RiskState> {
    override fun compareTo(other: RiskState) = risk.compareTo(other.risk)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RiskState) return false

        if (row != other.row) return false
        if (col != other.col) return false

        return true
    }

    override fun hashCode(): Int {
        var result = row
        result = 31 * result + col
        return result
    }
}

class RiskMap(private var grid: Array<IntArray>) {
    companion object {
        const val MAX_VALUE = 9
    }

    fun grow(size: Int) {
        grid = Array(grid.size * size) { row ->
            IntArray(grid[0].size * size) { col ->
                var value = grid[row % grid.size][col % grid[0].size] + (row / grid.size) + (col / grid[0].size)
                if (value > MAX_VALUE) {
                    value %= MAX_VALUE
                }
                value
            }
        }
    }

    fun findLeastRiskPath(
        startRow: Int = 0,
        startCol: Int = 0,
        endRow: Int = grid.size - 1,
        endCol: Int = grid[0].size - 1
    ): RiskState? {
        val visited = mutableSetOf<RiskState>()
        val queue = PriorityQueue<RiskState>()
        getNeighbors(RiskState(0, startRow, startCol)).addUnseenToQueue(queue, visited)

        while (queue.isNotEmpty()) {
            val currentState = queue.remove()
            if (currentState.row == endRow && currentState.col == endCol) {
                return currentState
            }

            getNeighbors(currentState).addUnseenToQueue(queue, visited)
        }

        return null
    }

    override fun toString() = with(StringBuilder()) {
        grid.forEach { row ->
            append(row.joinToString(""))
            append("\n")
        }

        toString().trim()
    }

    private fun List<RiskState>.addUnseenToQueue(queue: Queue<RiskState>, visited: MutableSet<RiskState>) = forEach {
        if (it !in visited) {
            visited.add(it)
            queue.add(it)
        }
    }

    private fun getNeighbors(riskState: RiskState) =
        mutableListOf<RiskState>().apply {
            with(riskState) {
                if (row > 0) add(RiskState(risk + grid[row - 1][col], row - 1, col))                // Up
                if (row < grid.size - 1) add(RiskState(risk + grid[row + 1][col], row + 1, col))    // Down
                if (col > 0) add(RiskState(risk + grid[row][col - 1], row, col - 1))                // Left
                if (col < grid[0].size - 1) add(RiskState(risk + grid[row][col + 1], row, col + 1)) // Right
            }
        }
}