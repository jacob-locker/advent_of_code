package day1

import InputRetriever
import org.junit.Test
import sweepList
import sweepListSlidingWindow
import kotlin.test.assertEquals

class Day1Test {
    private val inputRetriever = InputRetriever()

    @Test
    fun `part one`() {
        assertEquals(1139, sweepList(inputRetriever.retrieveInput(1)))
    }

    @Test
    fun `part two`() {
        assertEquals(1103, sweepListSlidingWindow(inputRetriever.retrieveInput(1)))
    }
}