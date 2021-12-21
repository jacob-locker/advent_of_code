package day20

import BaseDay
import getInputBlocks
import getInputLines

class Day20 : BaseDay(20, "Trench Map") {
    override suspend fun partOne(input: String) = solve(input, 2)

    override suspend fun partTwo(input: String) = solve(input, 50)

    private fun solve(input: String, iterations: Int) = solveDay20(input) { algorithm, image ->
        var newImage = image
        repeat(iterations) {
            newImage = newImage.applyAlgorithm(algorithm)
        }
        newImage.countLightPixels()
    }

    private fun solveDay20(input: String, solver: (ImageAlgorithm, Image) -> Int) = input.getInputBlocks().let {
        Pair(it[0].parseImageAlgorithm(), it[1].getInputLines().parseImage())
    }.let { solver(it.first, it.second) }
}

fun String.parseImageAlgorithm() = ImageAlgorithm(map { it == '#' }.toBooleanArray())

fun List<String>.parseImage() = Image(Array(size) { idx -> get(idx).map { it == '#' }.toBooleanArray() })

fun Array<BooleanArray>.toMap(): Map<Pair<Int, Int>, Boolean> = mutableMapOf<Pair<Int, Int>, Boolean>().apply {
    for (row in indices) {
        for (col in this@toMap[row].indices) {
            put(Pair(row, col), this@toMap[row][col])
        }
    }
}

fun Array<BooleanArray>.padded(padding: Int, fillValue: Boolean = false): Array<BooleanArray> {
    val padded = Array(size + 2 * padding) { BooleanArray(size + 2 * padding) { fillValue } }
    forEachIndexed { rowIdx, row ->
        row.forEachIndexed { colIdx, value ->
            padded[rowIdx + padding][colIdx + padding] = value
        }
    }
    return padded
}

fun Array<BooleanArray>.trim(amount: Int): Array<BooleanArray> {
    val trimmed = Array(size - 2 * amount) { BooleanArray(get(it).size - 2 * amount) { false } }
    for (row in amount until size - amount) {
        for (col in amount until get(row).size - amount) {
            trimmed[row - amount][col - amount] = get(row)[col]
        }
    }
    return trimmed
}

data class ImageAlgorithm(private val array: BooleanArray) {
    val size = array.size
    operator fun get(idx: Int): Boolean {
        return array[idx]
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ImageAlgorithm) return false

        if (!array.contentEquals(other.array)) return false

        return true
    }

    override fun hashCode(): Int {
        return array.contentHashCode()
    }
}

//class OtherImage(private val imageMap: Map<Pair<Int, Int>, Boolean>) {
//
//    fun applyAlgorithm(algorithm: ImageAlgorithm): OtherImage {
//        val newImageMap = imageMap.toMutableMap()
//        imageMap.forEach {
//            getAlgorithmIndex(it.key.first, it.key.second)
//        }
//    }
//
//    private fun getAlgorithmIndex(row: Int, col: Int) =
//        String(listOf(
//            imageMap.getOrDefault(Pair(row - 1, col - 1), false),
//            imageMap.getOrDefault(Pair(row - 1, col), false),
//            imageMap.getOrDefault(Pair(row - 1, col + 1), false),
//            imageMap.getOrDefault(Pair(row, col - 1), false),
//            imageMap.getOrDefault(Pair(row, col), false),
//            imageMap.getOrDefault(Pair(row, col + 1), false),
//            imageMap.getOrDefault(Pair(row + 1, col - 1), false),
//            imageMap.getOrDefault(Pair(row + 1, col), false),
//            imageMap.getOrDefault(Pair(row + 1, col + 1), false)
//        ).map { if (it) '1' else '0' }.toCharArray()
//        )
//            .toInt(2)
//
//}

enum class ImageState {
    PERMA_BLACK,
    PERMA_WHITE,
    BLACK_FLIP,
    WHITE_FLIP,
    UNKNOWN
}
// 5899 too high
// 5479 incorrect
// 5499 incorrect
// 5537 incorrect
// 5548 incorrect
// 5567 incorrect
// 5379 too low
data class Image(private val values: Array<BooleanArray>, private val initialState: ImageState = ImageState.UNKNOWN) {
    var state = when(initialState) {
        ImageState.PERMA_BLACK -> ImageState.PERMA_BLACK
        ImageState.PERMA_WHITE -> ImageState.PERMA_WHITE
        ImageState.BLACK_FLIP -> ImageState.WHITE_FLIP
        ImageState.WHITE_FLIP -> ImageState.BLACK_FLIP
        ImageState.UNKNOWN -> ImageState.UNKNOWN
    }

    fun applyAlgorithm(algorithm: ImageAlgorithm): Image {
        if (state == ImageState.UNKNOWN) {
            state = if (!algorithm[0]) {
                ImageState.PERMA_BLACK
            } else if (algorithm[0] && algorithm[algorithm.size - 1])  {
                ImageState.PERMA_WHITE
            } else {
                ImageState.BLACK_FLIP
            }
        }
        
        val defaultValue = state == ImageState.PERMA_WHITE || state == ImageState.WHITE_FLIP
        val paddedValues = values.padded(1, fillValue = defaultValue)
        val newValues = values.padded(1, fillValue = defaultValue)
        paddedValues.forEachIndexed { rowIdx, row ->
            row.forEachIndexed { colIdx, _ ->
                val algoIdx = getAlgorithmIndex(paddedValues, rowIdx, colIdx, defaultValue = defaultValue)
                newValues[rowIdx][colIdx] = algorithm[algoIdx]
            }
        }
        return Image(newValues, state)
    }

    fun countLightPixels() = values.sumOf { row -> row.count { it } }

    override fun toString() = with(StringBuilder()) {
        for (i in values.indices) {
            append(values[i].joinToString("") { if (it) "#" else "." })
            append("\n")
        }
        toString()
    }

    private fun getAlgorithmIndex(values: Array<BooleanArray>, row: Int, col: Int, defaultValue: Boolean = false) =
        String(listOf(
            if (row > 0 && col > 0) values[row - 1][col - 1] else defaultValue,
            if (row > 0) values[row - 1][col] else defaultValue,
            if (row > 0 && col < values[row].size - 1) values[row - 1][col + 1] else defaultValue,
            if (col > 0) values[row][col - 1] else defaultValue,
            values[row][col],
            if (col < values[row].size - 1) values[row][col + 1] else defaultValue,
            if (col > 0 && row < values.size - 1) values[row + 1][col - 1] else defaultValue,
            if (row < values.size - 1) values[row + 1][col] else defaultValue,
            if (row < values.size - 1 && col < values[row].size - 1) values[row + 1][col + 1] else defaultValue
        ).map { if (it) '1' else '0' }.toCharArray()
        ).toInt(2)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Image) return false

        if (!values.contentDeepEquals(other.values)) return false

        return true
    }

    override fun hashCode(): Int {
        return values.contentDeepHashCode()
    }
}