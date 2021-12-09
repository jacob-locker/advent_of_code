package day5

import DayRetriever
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class Day5Test {
//    private val simpleInput = DayRetriever().retrieveInput(5, test = true)
//    private val fullInput = DayRetriever().retrieveInput(5)
//
//    @Test
//    fun `part one simple`() {
//        assertEquals(5, findNumberOfPointsWithLineOverlap(simpleInput))
//    }
//
//    @Test
//    fun `part one`() {
//        assertEquals(8111, findNumberOfPointsWithLineOverlap(fullInput))
//    }
//
//    @Test
//    fun `part two simple`() {
//        assertEquals(12, findNumberOfPointsWithLineOverlap(simpleInput, ignoresDiagonals = false))
//    }
//
//    @Test
//    fun `part two`() {
//        assertEquals(22088, findNumberOfPointsWithLineOverlap(fullInput, ignoresDiagonals = false))
//    }

    @Test
    fun `ocean floor add right diagonal line top down`() {
        val oceanFloor = OceanFloor(3)
        oceanFloor.addVents(Vents(1, 1, 2, 2), ignoresDiagonals = false)
        oceanFloor.display()

        assertTrue(
            arrayOf(
                arrayOf(0, 0, 0),
                arrayOf(0, 1, 0),
                arrayOf(0, 0, 1)
            ).contentDeepEquals(oceanFloor.grid)
        )
    }

    @Test
    fun `ocean floor add right diagonal line bottom up`() {
        val oceanFloor = OceanFloor(3)
        oceanFloor.addVents(Vents(1, 1, 0, 0), ignoresDiagonals = false)
        oceanFloor.display()

        assertTrue(
            arrayOf(
                arrayOf(1, 0, 0),
                arrayOf(0, 1, 0),
                arrayOf(0, 0, 0)
            ).contentDeepEquals(oceanFloor.grid)
        )
    }

    @Test
    fun `ocean floor add left diagonal line top down`() {
        val oceanFloor = OceanFloor(3)
        oceanFloor.addVents(Vents(2, 0, 1, 1), ignoresDiagonals = false)
        oceanFloor.display()

        assertTrue(
            arrayOf(
                arrayOf(0, 0, 1),
                arrayOf(0, 1, 0),
                arrayOf(0, 0, 0)
            ).contentDeepEquals(oceanFloor.grid)
        )
    }

    @Test
    fun `ocean floor add left diagonal line bottom up`() {
        val oceanFloor = OceanFloor(3)
        oceanFloor.addVents(Vents(0, 2, 1, 1), ignoresDiagonals = false)
        oceanFloor.display()

        assertTrue(
            arrayOf(
                arrayOf(0, 0, 0),
                arrayOf(0, 1, 0),
                arrayOf(1, 0, 0)
            ).contentDeepEquals(oceanFloor.grid)
        )
    }
}