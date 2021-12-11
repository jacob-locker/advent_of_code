package day10

import org.junit.Test
import kotlin.test.assertEquals

class SyntaxScorerTest {

    @Test
    fun `check input lines`() {
        val scorer = SyntaxScorer()
        assertEquals(3, scorer.scoreCorruptedLine("[[<[([]))<([[{}[[()]]]"))
    }

    @Test
    fun `check input lines 2`() {
        val scorer = SyntaxScorer()
        assertEquals(1197, scorer.scoreCorruptedLine("{([(<{}[<>[]}>{[]{[(<()>"))
    }
}