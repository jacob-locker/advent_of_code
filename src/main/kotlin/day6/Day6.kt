package day6

import BaseDay

class Day6 : BaseDay(6, "Lanternfish") {
    override suspend fun partOne(input: String) = getNumberOfLanternFish(input, 80)

    override suspend fun partTwo(input: String) = getNumberOfLanternFish(input, 256)
}