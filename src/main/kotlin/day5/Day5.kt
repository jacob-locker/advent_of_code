package day5

import BaseDay

class Day5 : BaseDay(5, "Hydrothermal Venture") {
    override suspend fun partOne(input: String) = findNumberOfPointsWithLineOverlap(input)

    override suspend fun partTwo(input: String) = findNumberOfPointsWithLineOverlap(input, ignoresDiagonals = false)
}