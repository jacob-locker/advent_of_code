package day20

import BaseDayObjectTest
import getTestLines
import org.junit.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class Day20Test : BaseDayObjectTest(Day20(), partOneSimpleExpected = 35, partOneExpected = 5573, partTwoSimpleExpected=3351, partTwoExpected = 20097) {

    @Test
    fun `parse image algo`() {
        val algo = "..#.#"
        assertEquals(ImageAlgorithm(booleanArrayOf(false, false, true, false, true)), algo.parseImageAlgorithm())
    }

    @Test
    fun `parse input image`() {
        val image = """
            #..
            #..
            ##.
        """.trimIndent()
            .getTestLines()
            .parseImage()
        val expected = Image(
            arrayOf(
                booleanArrayOf(true, false, false),
                booleanArrayOf(true, false, false),
                booleanArrayOf(true, true, false)
            )
        )
        assertEquals(image, expected)
    }

    @Test
    fun `pad 2d array`() {
        val padded = arrayOf(
            booleanArrayOf(true, true, true),
            booleanArrayOf(true, true, true),
            booleanArrayOf(true, true, true)
        ).padded(2)

        val expected = arrayOf(
            booleanArrayOf(false, false, false, false, false, false, false),
            booleanArrayOf(false, false, false, false, false, false, false),
            booleanArrayOf(false, false, true,  true,  true,  false, false),
            booleanArrayOf(false, false, true,  true,  true,  false, false),
            booleanArrayOf(false, false, true,  true,  true,  false, false),
            booleanArrayOf(false, false, false, false, false, false, false),
            booleanArrayOf(false, false, false, false, false, false, false),
        )

        assertTrue(padded.contentDeepEquals(expected))
    }

    @Test
    fun `check image after one iteration`() {
        val algorithm = "..#.#..#####.#.#.#.###.##.....###.##.#..###.####..#####..#....#..#..##..###..######.###...####..#..#####..##..#.#####...##.#.#..#.##..#.#......#.###.######.###.####...#.##.##..#..#..#####.....#.#....###..#.##......#.....#..#..#..##..#...##.######.####.####.#.#...#.......#..#.#.#...####.##.#......#..#...##.#.##..#...##.#.##..###.#......#.#.......#.#.#.####.###.##...#.....####.#..#..#.##.#....##..#.####....##...##..#...#......#.#.......#.......##..####..#...#.#.#...##..#.#..###..#####........#..####......#..#".parseImageAlgorithm()

        val image = """
            #..#.
            #....
            ##..#
            ..#..
            ..###
        """.trimIndent().getTestLines().parseImage()

        var newImage = image.applyAlgorithm(algorithm)
        println(newImage)

        newImage = newImage.applyAlgorithm(algorithm)
        println(newImage)
    }
}