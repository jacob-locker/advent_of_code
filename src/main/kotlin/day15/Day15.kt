package day15

import BaseDay
import getInputLines
import java.util.*
import kotlin.math.abs
import kotlin.math.max


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

    private fun solveDay15(input: String, solver: (RiskMap) -> Int) =
        input.getInputLines().parseRiskMap().let { solver(it) }
}

fun List<String>.parseRiskMap() = RiskMap(map { it.map { char -> char.digitToInt() }.toIntArray() }.toTypedArray())

data class RiskState(val row: Int, val col: Int) : Comparable<RiskState> {
    var previousState: RiskState? = null
    var risk = 0
    var estimatedScore = 0
    override fun compareTo(other: RiskState) = estimatedScore.compareTo(other.estimatedScore)

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

class RiskMap(var grid: Array<IntArray>) {
    companion object {
        const val MAX_VALUE = 9
        val ZERO_HEURISTIC: (RiskState, RiskState) -> Int = { _, _ -> 0 }


        val CHEBYSHEV_HEURISTIC: (RiskState, RiskState) -> Int = { currentState, goalState ->
            max(abs(goalState.row - currentState.row), abs(goalState.col - currentState.col))
        }

        val MANHATTAN_HEURISTIC: (RiskState, RiskState) -> Int = { currentState, goalState ->
            abs(goalState.row - currentState.row) + abs(goalState.col - currentState.col)
        }
    }

    val size = grid.size
    val enteredList = mutableListOf<RiskState>()

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
        endCol: Int = grid[0].size - 1,
        heuristic: (RiskState, RiskState) -> Int = ZERO_HEURISTIC
    ) : RiskState? {
        val goal = RiskState(endRow, endCol)

        val open = PriorityQueue<RiskState>()
        open.add(RiskState(startRow, startCol))

        val openMap = Array(grid.size) { Array<RiskState?>(grid.size) { null } }
        openMap[startRow][startCol] = RiskState(startRow, startCol)

        val closedMap = Array(grid.size) { Array<RiskState?>(grid.size) { null } }

        while (open.isNotEmpty()) {
            val current = open.remove()
            closedMap[current.row][current.col] = current
            enteredList.add(current)
            if (current == goal) {
                return current
            }

            getNeighbors(current).forEach { neighbor ->
                val cost = current.risk + grid[neighbor.row][neighbor.col]
                if (openMap[neighbor.row][neighbor.col] != null && cost < openMap[neighbor.row][neighbor.col]!!.risk) {
                    open.remove(openMap[neighbor.row][neighbor.col])
                    openMap[neighbor.row][neighbor.col] = null
                }
                if (closedMap[neighbor.row][neighbor.col] != null && cost < closedMap[neighbor.row][neighbor.col]!!.risk) {
                    closedMap[neighbor.row][neighbor.col] = null
                }
                if (openMap[neighbor.row][neighbor.col] == null && closedMap[neighbor.row][neighbor.col] == null) {
                    neighbor.risk = cost
                    neighbor.estimatedScore = neighbor.risk + heuristic(neighbor, goal)
                    neighbor.previousState = current
                    open.add(neighbor)
                    openMap[neighbor.row][neighbor.col] = neighbor
                }
            }
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

    private fun List<RiskState>.addUnseenToQueue(
        queue: Queue<RiskState>,
        visited: MutableSet<RiskState>
    ) = forEach {
        if (it !in visited) {
            visited.add(it)
            queue.add(it)
        }
    }

    private fun getNeighbors(state: RiskState) =
        mutableListOf<RiskState>().apply {
            with(state) {
                // Up
                if (row > 0) add(RiskState(row - 1, col))

                // Down
                if (row < grid.size - 1) add(RiskState(row + 1, col))

                // Left
                if (col > 0) add(RiskState(row, col - 1))

                // Right
                if (col < grid[0].size - 1) add(RiskState(row, col + 1))
            }
        }
}