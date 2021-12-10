package day9

import BaseDay
import getInputLines
import okhttp3.internal.toImmutableList
import java.util.*

class Day9 : BaseDay(9, "Smoke Basin") {
    override suspend fun partOne(input: String) = solveDay9(input) { heightMap ->
        heightMap.findLowPointValues().let { it.sum() + it.size }
    }

    override suspend fun partTwo(input: String) = solveDay9(input) { heightMap ->
        heightMap.findBasinIndices()
            .sortedByDescending { it.size }
            .slice(0..2)
            .fold(1) { acc, set ->
                acc * set.size
            }
    }

    private fun solveDay9(input: String, solver: (HeightMap) -> Int) = input.getInputLines().map {
        it.toCharArray().map { char -> char.digitToInt() }
    }.let { solver.invoke(HeightMap(it)) }
}

class HeightMap(private val map: List<List<Int>>) {
    fun findLowPointValues() = findLowPoints().map { map[it.first][it.second] }

    fun findLowPoints() = mutableListOf<Pair<Int, Int>>().apply {
        for (row in map.indices) {
            for (col in map[row].indices) {
                if (getNeighbors(row, col).all { it > map[row][col] }) {
                    add(Pair(row, col))
                }
            }
        }
    }.toImmutableList()

    fun findBasinIndices() = findLowPoints().map { growBasin(it.first, it.second) }

    private fun growBasin(row: Int, col: Int): Set<Pair<Int, Int>> {
        val visited = mutableSetOf<Pair<Int, Int>>()
        val queue = LinkedList<Pair<Int, Int>>()
        queue.add(Pair(row, col))

        while (queue.isNotEmpty()) {
            val current = queue.pop()
            visited.add(current)
            getNeighborIndices(current.first, current.second)
                .filter { !visited.contains(it) && map[it.first][it.second] < 9 }
                .also {
                    visited.addAll(it)
                    queue.addAll(it)
                }
        }

        return visited
    }

    private fun getNeighborIndices(row: Int, col: Int) = arrayListOf<Pair<Int, Int>>().apply {
        if (row - 1 >= 0) add(Pair(row - 1, col))
        if (row + 1 < map.size) add(Pair(row + 1, col))
        if (col - 1 >= 0) add(Pair(row, col - 1))
        if (col + 1 < map[0].size) add(Pair(row, col + 1))
    }

    private fun getNeighbors(row: Int, col: Int) = getNeighborIndices(row, col).map { map[it.first][it.second] }
}