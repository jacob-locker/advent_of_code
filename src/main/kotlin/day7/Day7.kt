package day7

import BaseDay

class Day7 : BaseDay(7, "The Treachery of Whales") {
    override suspend fun partOne(input: String) = minFuelCostToAlignCrabs(input)
    override suspend fun partTwo(input: String) = minFuelCostToAlignCrabs(input, heuristic = Heuristic.GaussSum)
}