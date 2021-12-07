package day7

import BaseDayTest

class Day7Test : BaseDayTest<Int>(
    7,
    partOneMethod = ::minFuelCostToAlignCrabs,
    partOneSimpleExpected = 37,
    partOneExpected = 353800,
    partTwoMethod = { minFuelCostToAlignCrabs(it, heuristic = Heuristic.CrabFuelCost) },
    partTwoSimpleExpected = 168,
    partTwoExpected = 98119739
)