package day8

import org.junit.Test
import kotlin.test.assertEquals

class SevenSegmentDisplayTest {
    @Test
    fun `check find segments`() {
        checkSegments(
            "acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab | cdfeb fcadb cdfeb cdbaf",
            listOf(5, 3, 5, 3)
        )
        checkSegments(
            "abcefg cf acdeg acdfg bcdf abdfg abdfeg acf abcdefg abcdfg | abcefg cf acdeg acdfg bcdf abdfg abdfeg acf abcdefg abcdfg",
            IntRange(0, 9).toList()
        )
    }

    private fun checkSegments(testInput: String, expected: List<Int>) {
        val testDisplays = testInput.parseDisplays()
        val testOutput = testInput.parseOutputs()

        val sevenSegmentDisplay = SevenSegmentDisplay(testDisplays)
        assertEquals(expected, sevenSegmentDisplay.getDisplayOutput(testOutput))
    }

    fun String.parseDisplays() = split(" | ")[0].split(" ").map { it.toCharArray().toSet() }
    fun String.parseOutputs() = split(" | ")[1].split(" ").map { it.toCharArray().toSet() }
}