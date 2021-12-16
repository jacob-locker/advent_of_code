package day14

import BaseDay
import getInputBlocks
import getInputLines
import java.lang.StringBuilder

class Day14 : BaseDay(14, "Extended Polymerization") {
    override suspend fun partOne(input: String) = solveDay14(input) { template, rules ->
        findMaxMinusMin(Template(template, rules), 10)
    }

    override suspend fun partTwo(input: String) = solveDay14(input) { template, rules ->
        findMaxMinusMin(Template(template, rules), 40)
    }

    private fun solveDay14(input: String, solver: (String, Map<String, Char>) -> Long) = input.getInputBlocks()
        .parseTemplateAndRules()
        .let { solver(it.first, it.second) }

    private fun findMaxMinusMin(template: Template, steps: Int) =
        with(template) {
            repeat(steps) { applyRules() }
            elementQuantities.let { freqMap ->
                freqMap.maxOf { it.value } - freqMap.minOf { it.value }
            }
        }
}

fun List<String>.parseTemplateAndRules(lineSplitter: String.() -> List<String> = String::getInputLines) = let { blocks ->
    Pair(blocks[0], mutableMapOf<String, Char>().apply {
        blocks[1].lineSplitter().forEach {
            put(it.substring(0, 2), it.split(" -> ")[1][0])
        }
    })
}

class Template(templateString: String, val rules: Map<String, Char>) {

    private var ruleQuantities = mutableMapOf<String, Long>().apply {
        templateString.windowed(2, 1) { put(it.toString(), getOrDefault(it.toString(), 0) + 1) }
    }

    val elementQuantities = mutableMapOf<Char, Long>().apply {
        templateString.forEach { put(it, getOrDefault(it, 0) + 1) }
    }

    fun applyRules() {
        val newMap = mutableMapOf<String, Long>()

        ruleQuantities.forEach {
            val insertionChar = rules[it.key]!!
            val firstString = String(charArrayOf(it.key[0], insertionChar))
            val secondString = String(charArrayOf(insertionChar, it.key[1]))

            elementQuantities.apply { put(insertionChar, getOrDefault(insertionChar, 0) + it.value) }

            newMap.apply {
                put(firstString, getOrDefault(firstString, 0) + it.value)
                put(secondString, getOrDefault(secondString, 0) + it.value)
            }
        }

        ruleQuantities = newMap
    }
}