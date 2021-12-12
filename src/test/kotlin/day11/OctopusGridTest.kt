package day11

import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class OctopusGridTest {

    private lateinit var testOctopusGrid: OctopusGrid

    @Before
    fun setUp() {
        testOctopusGrid = """
            5483143223
            2745854711
            5264556173
            6141336146
            6357385478
            4167524645
            2176841721
            6882881134
            4846848554
            5283751526
        """.trimIndent().parseOctopusGridFromTestInput()
    }

    @Test
    fun `parses string to octopus grid`() {
        val stringInput = """
            5483143223
            2745854711
            5264556173
            6141336146
            6357385478
            4167524645
            2176841721
            6882881134
            4846848554
            5283751526
        """.trimIndent().split("\n")
        stringInput.parseOctopusGrid()
        println(stringInput.parseOctopusGrid())
    }

    @Test
    fun `after one step`() {
        val expected = """
            6594254334
            3856965822
            6375667284
            7252447257
            7468496589
            5278635756
            3287952832
            7993992245
            5957959665
            6394862637
        """.trimIndent().parseOctopusGridFromTestInput()

        testOctopusGrid.step()

        assertEquals(expected, testOctopusGrid)
    }

    @Test
    fun `after two steps`() {
        val expected = """
            8807476555
            5089087054
            8597889608
            8485769600
            8700908800
            6600088989
            6800005943
            0000007456
            9000000876
            8700006848
        """.trimIndent().parseOctopusGridFromTestInput()

        testOctopusGrid.stepTimes(2)

        assertEquals(expected, testOctopusGrid)
    }

    @Test
    fun `after three steps`() {
        val expected = """
            0050900866
            8500800575
            9900000039
            9700000041
            9935080063
            7712300000
            7911250009
            2211130000
            0421125000
            0021119000
        """.trimIndent().parseOctopusGridFromTestInput()

        testOctopusGrid.stepTimes(3)

        assertEquals(expected, testOctopusGrid)
    }

    @Test
    fun `after four steps`() {
        val expected = """
            2263031977
            0923031697
            0032221150
            0041111163
            0076191174
            0053411122
            0042361120
            5532241122
            1532247211
            1132230211
        """.trimIndent().parseOctopusGridFromTestInput()

        testOctopusGrid.stepTimes(4)

        assertEquals(expected, testOctopusGrid)
    }

    @Test
    fun `after five steps`() {
        val expected = """
            4484144000
            2044144000
            2253333493
            1152333274
            1187303285
            1164633233
            1153472231
            6643352233
            2643358322
            2243341322
        """.trimIndent().parseOctopusGridFromTestInput()

        testOctopusGrid.stepTimes(5)

        assertEquals(expected, testOctopusGrid)
    }

    @Test
    fun `after ten steps`() {
        val expected = """
            0481112976
            0031112009
            0041112504
            0081111406
            0099111306
            0093511233
            0442361130
            5532252350
            0532250600
            0032240000
        """.trimIndent().parseOctopusGridFromTestInput()

        testOctopusGrid.stepTimes(10)

        assertEquals(expected, testOctopusGrid)
        assertEquals(204, testOctopusGrid.numberOfFlashes)
    }

    @Test
    fun `after 100 steps`() {
        val expected = """
            0397666866
            0749766918
            0053976933
            0004297822
            0004229892
            0053222877
            0532222966
            9322228966
            7922286866
            6789998766
        """.trimIndent().parseOctopusGridFromTestInput()

        testOctopusGrid.stepTimes(100)

        assertEquals(expected, testOctopusGrid)
        assertEquals(1656, testOctopusGrid.numberOfFlashes)
    }

    private fun String.parseOctopusGridFromTestInput() = split("\n").parseOctopusGrid()
}