package day11

import BaseDay
import getInputLines
import java.lang.StringBuilder
import java.util.*

class Day11 : BaseDay(11, "Dumbo Octopus") {
    override suspend fun partOne(input: String) = solveDay11(input) {
        it.stepTimes(100)
        it.numberOfFlashes
    }

    override suspend fun partTwo(input: String) = solveDay11(input) {
        var steps = 0L
        while (!it.allOctopusesAreFlashing()) {
            it.step()
            steps++
        }
        steps
    }

    private fun solveDay11(input: String, solver: (OctopusGrid) -> Long) =
        input.getInputLines().parseOctopusGrid().let(solver)
}

fun List<String>.parseOctopusGrid() = let { rows ->
    OctopusGrid(Array(rows.size) { rowIndex -> IntArray(rows[rowIndex].length) { colIndex -> rows[rowIndex][colIndex].digitToInt() } })
}

class OctopusGrid(private val grid: Array<IntArray>, private var _numberOfFlashes: Long = 0L) {

    val values get() = grid
    val numberOfFlashes
    get() = _numberOfFlashes

    val size
    get() = grid.size

    fun stepTimes(times: Int) = repeat(times) { step() }

    fun step() {
        val flashQueue = LinkedList<Pair<Int, Int>>()
        val flashers = mutableSetOf<Pair<Int, Int>>()
        // First, add one energy to all items in the grid adding octopuses with > 9 energy to the flash list
        grid.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { colIndex, _ ->
                grid[rowIndex][colIndex] += 1
                if (grid[rowIndex][colIndex] > 9) {
                    flashQueue.add(Pair(rowIndex, colIndex))
                    flashers.add(Pair(rowIndex, colIndex))
                }
            }
        }

        // Second, execute flashes on octopuses > 9 energy
        while (flashQueue.isNotEmpty()) {
            val flasher = flashQueue.removeFirst()
            getNeighbors(flasher.first, flasher.second).forEach {
                grid[it.first][it.second] += 1
                if (!flashers.contains(it) && grid[it.first][it.second] > 9) {
                    flashers.add(it)
                    flashQueue.add(it)
                }
            }
        }

        // Last, set all flashers to zero
        flashers.forEach { grid[it.first][it.second] = 0 }
        _numberOfFlashes += flashers.size
    }

    fun allOctopusesAreFlashing() = grid.all { it.all { value -> value == 0 } }

    private fun getNeighbors(row: Int, col: Int) = arrayListOf<Pair<Int, Int>>().apply {
        if (row > 0) add(Pair(row - 1, col))
        if (row < grid.size - 1) add(Pair(row + 1, col))
        if (col > 0) add(Pair(row, col - 1))
        if (col < grid[row].size - 1) add(Pair(row, col + 1))

        if (row > 0 && col > 0) add(Pair(row - 1, col - 1))
        if (row < grid.size - 1 && col > 0) add(Pair(row + 1, col - 1))
        if (row > 0 && col < grid[row].size - 1) add(Pair(row - 1, col + 1))
        if (row < grid.size - 1 && col < grid[row].size - 1) add(Pair(row + 1, col + 1))
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OctopusGrid

        if (!grid.contentDeepEquals(other.grid)) return false

        return true
    }

    override fun hashCode(): Int {
        return grid.contentDeepHashCode()
    }

    override fun toString() =
        with(StringBuilder()) {
            for (i in grid.indices) {
                append(grid[i].contentToString())
                append("\n")
            }
            toString()
        }
}