package day3

import BaseDay

class Day3 : BaseDay(3, "Binary Diagnostic") {
    override suspend fun partOne(input: String) = powerConsumption(input)

    override suspend fun partTwo(input: String) = lifeSupportRating(input)
}