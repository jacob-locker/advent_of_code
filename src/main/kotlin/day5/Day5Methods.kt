package day5

import getInputLines

fun solveDay5(input: String, solver: (List<Vents>) -> Int) = input.getInputLines().parseVentList().let(solver)

fun findNumberOfPointsWithLineOverlap(input: String, ignoresDiagonals: Boolean = true) = solveDay5(input) { lines ->
    with(OceanFloor(1000)) {
        lines.forEach { addVents(it, ignoresDiagonals) }
        getOverlaps()
    }
}