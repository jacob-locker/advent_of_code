package day13

import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class PaperTest {

    private lateinit var paper: Paper

    @Before
    fun setUp() {
        paper = """
            6,10
            0,14
            9,10
            0,3
            10,4
            4,11
            6,0
            6,12
            4,1
            0,13
            10,12
            3,4
            3,0
            8,4
            1,10
            2,14
            8,10
            9,0
        """.trimIndent().split("\n").parsePaper()
    }

    @Test
    fun `parse paper correctly`() {
        assertEquals("""
            ...#..#..#.
            ....#......
            ...........
            #..........
            ...#....#.#
            ...........
            ...........
            ...........
            ...........
            ...........
            .#....#.##.
            ....#......
            ......#...#
            #..........
            #.#........
        """.trimIndent(), paper.toString())
    }

    @Test
    fun `check fold horizontally`() {
        paper.foldAlongHorizontal(7)

        assertEquals("""
            #.##..#..#.
            #...#......
            ......#...#
            #...#......
            .#.#..#.###
            ...........
            ...........
        """.trimIndent(), paper.toString())

        assertEquals(17, paper.numberOfDots)
    }

    @Test
    fun `check fold vertically`() {
        paper.foldAlongHorizontal(7)
        paper.foldAlongVertical(5)

        assertEquals("""
            #####
            #...#
            #...#
            #...#
            #####
            .....
            .....
        """.trimIndent(), paper.toString())
    }
}