package day4

import BaseDay

class Day4 : BaseDay(4, "Giant Squid") {
    override suspend fun partOne(input: String) = findWinningScore(input)

    override suspend fun partTwo(input: String) = findLastWinningScore(input)
}