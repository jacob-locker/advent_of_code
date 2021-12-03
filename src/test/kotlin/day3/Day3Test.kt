package day3

import InputRetriever
import org.junit.Test
import kotlin.test.assertEquals

class Day3Test {
    val simpleInput = InputRetriever().retrieveInput(3, test = true)
    val fullInput = InputRetriever().retrieveInput(3)

    @Test
    fun `part one simple`() {
        assertEquals(198, powerConsumption(simpleInput))
    }

    @Test
    fun `part one`() {
        assertEquals(3277364, powerConsumption(fullInput))
    }

    @Test
    fun `part two simple`() {
        assertEquals(230, lifeSupportRating(simpleInput))
    }

    @Test
    fun `part two`() {
        assertEquals(5736383, lifeSupportRating(fullInput))
    }
}