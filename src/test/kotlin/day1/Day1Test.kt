package day1

import DayRetriever
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals

class Day1Test {
    private val dayRetriever = DayRetriever()

    @Test
    fun `part one`() {
        runBlocking { assertEquals(1139, sweepList(dayRetriever.retrieveInput(1))) }
    }

    @Test
    fun `part two`() {
        runBlocking { assertEquals(1103, sweepListSlidingWindow(dayRetriever.retrieveInput(1))) }
    }
}