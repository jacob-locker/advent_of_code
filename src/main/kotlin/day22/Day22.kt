package day22

import BaseDay
import getInputLines

class Day22 : BaseDay(22, "Reactor Reboot") {
    override suspend fun partOne(input: String) = solveDay22(input) { instructions ->
        executeInstructions(instructions.filter { it.range3D.inRange(Range3D(-50L..50L, -50L..50L, -50L..50L)) })
    }

    override suspend fun partTwo(input: String) = solveDay22(input) { executeInstructions(it) }

    private fun executeInstructions(instructions: List<Instruction>) = with(Reactor()) {
        instructions.forEach { executeInstruction(it) }
        activeCubeCount
    }

    private fun solveDay22(input: String, solver: (List<Instruction>) -> Long) =
        input.getInputLines().parseInstructions().let(solver)
}

fun List<String>.parseInstructions() = map {
    Instruction(it.split(" ")[0] == "on", Range3D(it.parseRange(0), it.parseRange(1), it.parseRange(2)))
}

fun String.parseRange(idx: Int) = split(",")[idx].split("=")[1].split("..").let {
    LongRange(it[0].toLong(), it[1].toLong())
}

/**
 * Gets the common overlapping range between two ranges in O(1).  Noticed that using the intersect() method causes dramatic slowdown
 */
fun LongRange.overlap(other: LongRange) =
    // [        ]
    //   [    ]
    if (first < other.first && last > other.last) {
        other.first..other.last
    }
    //   [    ]
    // [         ]
    else if (other.first < first && other.last > last) {
        first..last
    }
    // [   ]
    // [   ]
    else if (first == other.first && last == other.last) {
        first..last
    }
    // [     ]
    // [   ]
    else if (first == other.first && other.last < last) {
        first..other.last
    }
    // [   ]
    // [     ]
    else if (first == other.first && last < other.last) {
        first..last
    }
    // [     ]
    //   [   ]
    else if (last == other.last && first < other.first) {
        other.first..last
    }
    //   [   ]
    // [     ]
    else if (last == other.last && other.first < first) {
        first..last
    }
    // [   ]
    //   [   ]
    else if (first < other.first && last < other.last && last > other.first) {
        other.first..last
    }
    //   [   ]
    // [   ]
    else if (first > other.first && last > other.last && first < other.last) {
        first..other.last
    }
    // [    ]
    //      [    ]
    else if (last == other.first) {
        last..last
    }
    //      [    ]
    // [    ]
    else if (first == other.last) {
        first..first
    }
    else {
        null
    }

/**
 * Removes the range and gives back the list of remaining ranges.  Empty list if no remaining ranges
 */
fun LongRange.remove(other: LongRange) =
    overlap(other)?.let { overlap ->
        if (overlap.first <= first && overlap.last >= last) {
            emptyList()
        } else {
            mutableListOf<LongRange>().apply {
                if (overlap.first > this@remove.first) {
                    add(first until overlap.first)
                }
                if (overlap.last < this@remove.last) {
                    add((overlap.last + 1)..last)
                }
            }
        }
    } ?: listOf(first..last)

fun LongRange.inRange(other: LongRange) = first >= other.first && last <= other.last

fun Collection<Range3D>.sumOfCubes() =
    sumOf { (it.xRange.last - it.xRange.first + 1) * (it.yRange.last - it.yRange.first + 1) * (it.zRange.last - it.zRange.first + 1) }

data class Range3D(val xRange: LongRange, val yRange: LongRange, val zRange: LongRange) {
    fun inRange(other: Range3D) = xRange.inRange(other.xRange) && yRange.inRange(other.yRange) && zRange.inRange(other.zRange)

    private fun overlap(other: Range3D) = xRange.overlap(other.xRange)?.let { xOverlap ->
        yRange.overlap(other.yRange)?.let { yOverlap ->
            zRange.overlap(other.zRange)?.let { zOverlap ->
                Range3D(xOverlap, yOverlap, zOverlap)
            }
        }
    }

    /**
     * Removes a range of 3D values from this 3D range and gives back a list of remaining 3D ranges
     */
    fun remove(other: Range3D) = overlap(other)?.let { overlap ->
        mutableSetOf<Range3D>().apply {
            // Left and Right "slabs"
            addAll(xRange.remove(overlap.xRange).map { x -> Range3D(x, yRange, zRange) })
            // Top and Bottom "slabs"
            addAll(yRange.remove(overlap.yRange).map { y -> Range3D(overlap.xRange, y, zRange) })
            // Front and Back "slabs"
            addAll(zRange.remove(overlap.zRange).map { z -> Range3D(overlap.xRange, overlap.yRange, z) })
        }
    } ?: listOf(Range3D(xRange, yRange, zRange))
}

data class Instruction(val on: Boolean, val range3D: Range3D)

class Reactor {
    private var activeRanges = mutableListOf<Range3D>()

    val activeCubeCount get() = activeRanges.sumOfCubes()

    fun executeInstruction(instruction: Instruction) {
        if (instruction.on) {
            turnOnRange(instruction.range3D)
        } else {
            turnOffRange(instruction.range3D)
        }
    }

    private fun turnOnRange(instRange3D: Range3D) {
        turnOffRange(instRange3D)
        activeRanges.add(instRange3D)
    }

    private fun turnOffRange(instRange3D: Range3D) {
        activeRanges = activeRanges.flatMap {
            it.remove(instRange3D)
        }.toMutableList()
    }
}