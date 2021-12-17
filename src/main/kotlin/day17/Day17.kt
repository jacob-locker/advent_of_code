package day17

import BaseDay

class Day17 : BaseDay(17, "Trick Shot") {
    override suspend fun partOne(input: String) = solveDay17(input) { targetArea ->
       launchTonsOfProbes(targetArea).first
    }

    override suspend fun partTwo(input: String) = solveDay17(input) { targetArea ->
        launchTonsOfProbes(targetArea).second
    }

    private fun solveDay17(input: String, solver: (TargetArea) -> Int) = input.parseTargetArea().let(solver)

    private fun launchTonsOfProbes(targetArea: TargetArea) : Pair<Int, Int> {
        val probes = mutableSetOf<Probe>().apply {
            var id = 0
            for (xVel in 1..250) {
                for (yVel in -200..200) {
                    add(Probe(id = id++, initialVelocity = Pair(xVel, yVel)))
                }
            }
        }

        var highestYPos = 0
        val collidedProbes = mutableSetOf<Probe>()
        while (probes.isNotEmpty()) {
            val probesToRemove = mutableListOf<Probe>()
            probes.forEach { probe ->
                probe.step()
                if (probe.position in targetArea && probe.highestYPos > highestYPos) {
                    highestYPos = probe.highestYPos
                }
                if (probe.position in targetArea) {
                    collidedProbes.add(probe)
                }
                if (probe.position !in targetArea &&
                    (probe.position.first > targetArea.xRange.last || probe.position.second < targetArea.yRange.first)) {
                    probesToRemove.add(probe)
                }
            }

            probesToRemove.forEach { probes.remove(it) }
        }
        return Pair(highestYPos, collidedProbes.size)
    }
}

fun String.parseTargetArea() = split("x=")[1].split(", y=").let {
    val xRange = it[0].split("..").let { range -> IntRange(range[0].toInt(), range[1].toInt()) }
    val yRange = it[1].split("..").let { range -> IntRange(range[0].toInt(), range[1].toInt()) }

    TargetArea(xRange, yRange)
}

class TargetArea(val xRange: IntRange, val yRange: IntRange) {
    operator fun contains(position: Pair<Int, Int>): Boolean {
        return position.first in xRange && position.second in yRange
    }
}
class Probe(val id: Int, initialPosition: Pair<Int, Int> = Pair(0, 0), val initialVelocity: Pair<Int, Int>) {
    val position
    get() = _position
    private var _position = initialPosition

    val velocity
    get() = _velocity
    private var _velocity = initialVelocity

    val highestYPos
    get() = _highestYPos
    private var _highestYPos = initialPosition.second

    fun step() {
        _position = Pair(_position.first + _velocity.first, _position.second + _velocity.second)
        if (_position.second > _highestYPos) {
            _highestYPos = _position.second
        }
        _velocity = Pair(_velocity.first.plusEffectsOfDrag(), _velocity.second - 1)
    }

    private fun Int.plusEffectsOfDrag() = if (this < 0) this + 1 else if (this > 0) this - 1 else 0
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Probe) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }
}