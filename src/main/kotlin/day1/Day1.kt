package day1

import BaseDay

class Day1 : BaseDay(1, "Sonar Scan") {
    override suspend fun partOne(input: String) = sweepList(input)

    override suspend fun partTwo(input: String) = sweepListSlidingWindow(input)
}