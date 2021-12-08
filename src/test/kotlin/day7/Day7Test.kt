package day7

import BaseDayObjectTest
import BaseDayTest
import org.junit.Test
import kotlin.system.measureTimeMillis

class Day7Test : BaseDayObjectTest(
    Day7(),
    partOneSimpleExpected = 37,
    partOneExpected = 353800,
    partTwoSimpleExpected = 168,
    partTwoExpected = 98119739
) {
    @Test
    fun `compare heuristic performance`() {
        val recursiveSumTime = measureTimeMillis {
            minFuelCostToAlignCrabs(fullInput, heuristic = Heuristic.RecursiveSum)
        }
        val gaussSumTime = measureTimeMillis {
            minFuelCostToAlignCrabs(fullInput, heuristic = Heuristic.GaussSum)
        }
        println("Recursive Heuristic takes: ${recursiveSumTime}ms")
        println("Gaussian Sum Heuristic takes: ${gaussSumTime}ms")
    }
}