package day8

import BaseDay
import getInputLines
import java.lang.StringBuilder

class Day8 : BaseDay(8, "Seven Segment Search") {
    override suspend fun partOne(input: String) = solveDay8(input) { displaysAndOutputs ->
        // Sum of 1s, 4s, 7s, and 8s
        displaysAndOutputs.fold(0) { acc, displayAndOutput ->
            acc + SevenSegmentDisplay(displayAndOutput.first).let {
                it.getDisplayOutput(displayAndOutput.second).count { num -> num == 1 || num == 4 || num == 7 || num == 8 }
            }
        }
    }

    override suspend fun partTwo(input: String) = solveDay8(input) { displaysAndOutputs ->
        // Sum of all outputs
        displaysAndOutputs.fold(0) { acc, displayAndOutput ->
            acc + SevenSegmentDisplay(displayAndOutput.first).let { sevenSegmentDisplay ->
                StringBuilder().apply {
                    sevenSegmentDisplay.getDisplayOutput(displayAndOutput.second).dropWhile { it == 0 }.forEach { append(it) }
                }.toString().toInt()
            }
        }
    }

    private fun solveDay8(input: String, solver: (List<Pair<List<Set<Char>>, List<Set<Char>>>>) -> Int) = input.getInputLines().map {
        val displayAndOutput = it.split(" | ")
        Pair(displayAndOutput[0].split(" ").map { it.toCharArray().toSet() }, displayAndOutput[1].split(" ").map { it.toCharArray().toSet() })
    }.let(solver)
}

class SevenSegmentDisplay(display: List<Set<Char>>) {
    private val aSeg: Char
    private val bSeg: Char
    private val cSeg: Char
    private val dSeg: Char
    private val eSeg: Char
    private val fSeg: Char
    private val gSeg: Char

    private val zero: Set<Char>
    private val one: Set<Char>
    private val two: Set<Char>
    private val three: Set<Char>
    private val four: Set<Char>
    private val five: Set<Char>
    private val six: Set<Char>
    private val seven: Set<Char>
    private val eight: Set<Char>
    private val nine: Set<Char>

    init {
        val totalFreqMap = display.flatten().groupBy { it }.toMutableMap()
        eSeg = totalFreqMap.entries.find { it.value.size == 4 }?.key?.also { totalFreqMap.remove(it) }!!
        fSeg = totalFreqMap.entries.find { it.value.size == 9 }?.key?.also { totalFreqMap.remove(it) }!!
        bSeg = totalFreqMap.entries.find { it.value.size == 6 }?.key?.also { totalFreqMap.remove(it) }!!

        val oneString = display.find { it.size == 2 }

        aSeg = display.find { it.size == 3 }?.find { oneString?.contains(it) == false }?.also { totalFreqMap.remove(it) }!!
        cSeg = totalFreqMap.entries.find { it.value.size == 8 }?.key?.also { totalFreqMap.remove(it) }!!
        dSeg = display.find { it.size == 4 }?.toMutableSet()?.apply {
            remove(bSeg)
            remove(cSeg)
            remove(fSeg)
        }?.first().also { totalFreqMap.remove(it) }!!
        gSeg = totalFreqMap[totalFreqMap.keys.first()]?.first()!!

        zero = setOf(aSeg, bSeg, cSeg, eSeg, fSeg, gSeg)
        one = setOf(cSeg, fSeg)
        two = setOf(aSeg, cSeg, dSeg, eSeg, gSeg)
        three = setOf(aSeg, cSeg, dSeg, fSeg, gSeg)
        four = setOf(bSeg, cSeg, dSeg, fSeg)
        five = setOf(aSeg, bSeg, dSeg, fSeg, gSeg)
        six = setOf(aSeg, bSeg, dSeg, fSeg, eSeg, gSeg)
        seven = setOf(aSeg, cSeg, fSeg)
        eight = setOf(aSeg, bSeg, cSeg, dSeg, eSeg, fSeg, gSeg)
        nine = setOf(aSeg, bSeg, cSeg, dSeg, fSeg, gSeg)
    }

    fun getDisplayOutput(output: List<Set<Char>>) = output.map {
        if (zero.containsAll(it) && it.containsAll(zero)) {
            0
        } else if (one.containsAll(it) && it.containsAll(one)) {
            1
        } else if (two.containsAll(it) && it.containsAll(two)) {
            2
        } else if (three.containsAll(it) && it.containsAll(three)) {
            3
        } else if (four.containsAll(it) && it.containsAll(four)) {
            4
        } else if (five.containsAll(it) && it.containsAll(five)) {
            5
        } else if (six.containsAll(it) && it.containsAll(six)) {
            6
        } else if (seven.containsAll(it) && it.containsAll(seven)) {
            7
        } else if (eight.containsAll(it) && it.containsAll(eight)) {
            8
        } else if (nine.containsAll(it) && it.containsAll(nine)){
            9
        } else {
            -1
        }
    }
}
