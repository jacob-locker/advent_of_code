package day3

import getInputLines
import java.lang.Byte.parseByte
import java.lang.Integer.parseInt

fun parseDay3Input(input: String) = input.getInputLines()

fun powerConsumption(input: String) = powerConsumption(parseDay3Input(input))
fun powerConsumption(input: List<String>) : Int {
    val bitCount = getBitCount(input)

    val gammaBuilder = StringBuilder("")
    val epsilonBuilder = StringBuilder("")
    bitCount.forEach { count ->
        gammaBuilder.append(if (count > 0) "1" else "0")
        epsilonBuilder.append(if (count > 0) "0" else "1")
    }
    val gamma = parseInt(gammaBuilder.toString(), 2)
    val epsilon = parseInt(epsilonBuilder.toString(), 2)

    return gamma * epsilon
}

fun lifeSupportRating(input: String) = lifeSupportRating(parseDay3Input(input))
fun lifeSupportRating(input: List<String>) : Int {
    val bitCount = getBitCount(input)
    val oxygenRating = findRating(input, bitCount) { if (it >= 0) '1' else '0' }
    val co2Rating = findRating(input, bitCount) { if (it >= 0) '0' else '1' }

    return oxygenRating * co2Rating
}

fun getBitCount(input: List<String>) : List<Int> {
    val bitCount = MutableList(input[0].length) { 0 }
    input.forEach { binaryString ->
        binaryString.forEachIndexed { bitPosition, value ->
            bitCount[bitPosition] += if (value == '1') 1 else -1
        }
    }
    return bitCount
}

fun findRating(input: List<String>, bitCount: List<Int>, bitFilter: (Int) -> Char) : Int {
    var filteredList: List<String> = input

    bitCount.forEachIndexed { bitPosition, count ->
        if (filteredList.size > 1) {
            filteredList = filteredList.filter { it[bitPosition] == bitFilter(getBitCount(filteredList)[bitPosition]) }
        }
    }

    return parseInt(filteredList.single(), 2)
}