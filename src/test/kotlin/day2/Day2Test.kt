package day2

import InputRetriever
import org.junit.Test
import kotlin.test.assertEquals

class Day2Test {
    private val inputRetriever = InputRetriever()
    private val simpleInput = inputRetriever.retrieveInput(2, test = true)

    @Test
    fun `part one simple`() {
        assertEquals(150, dive(simpleInput))
    }

    @Test
    fun `part two simple`() {
        assertEquals(900, diveWithAim(simpleInput))
    }
}