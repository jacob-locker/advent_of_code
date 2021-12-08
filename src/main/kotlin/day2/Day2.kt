package day2

import BaseDay

class Day2 : BaseDay(2, "Dive!") {
    override suspend fun partOne(input: String) = dive(input)

    override suspend fun partTwo(input: String) = diveWithAim(input)
}