package day13

import BaseDay
import getInputLines
import java.lang.StringBuilder

class Day13 : BaseDay(13, "Transparent Origami") {
    override suspend fun partOne(input: String) = solveDay13(input) { paper, instructions ->
        executeInstruction(paper, instruction = instructions.first())
        paper.numberOfDots
    }

    override suspend fun partTwo(input: String) = solveDay13(input) { paper, instructions ->
        println(instructions)
        instructions.forEach { executeInstruction(paper, it) }
        paper.numberOfDots.also { paper.display() }
    }

    private fun executeInstruction(paper: Paper, instruction: Instruction) = when (instruction.type) {
        InstructionType.FOLD_HORIZONTALLY -> paper.foldAlongHorizontal(instruction.position)
        InstructionType.FOLD_VERTICALLY -> paper.foldAlongVertical(instruction.position)
    }

    private fun solveDay13(input: String, solver: (Paper, List<Instruction>) -> Int) = input.split(System.lineSeparator() + System.lineSeparator()).let {
        Pair(it[0].getInputLines().parsePaper(), it[1].getInputLines().parseInstructions())
    }.let { solver(it.first, it.second) }
}

fun List<String>.parsePaper() = Paper(map {
    it.split(",").let { line ->
        Pair(line[0].toInt(), line[1].toInt())
    }
}.toSet())

fun List<String>.parseInstructions() = map { it.parseInstruction() }
fun String.parseInstruction() = split("=").let {
    Instruction(if (it[0].last() == 'x') InstructionType.FOLD_VERTICALLY else InstructionType.FOLD_HORIZONTALLY, it.last().toInt())
}

class Paper(val points: Set<Pair<Int, Int>>) {
    private val initialHeight = points.maxOf { it.second } + 1
    private val initialWidth = points.maxOf { it.first } + 1
    private var sheet =
        MutableList(initialHeight) { y -> MutableList(initialWidth) { x -> if (Pair(x, y) in points) 1 else 0 } }

    val numberOfDots get() = sheet.sumOf { it.count { num -> num > 0 } }

    fun foldAlongHorizontal(horizontal: Int = sheet.size / 2) {
        for (row in sheet.size - 1 downTo horizontal) {
            for (col in 0 until sheet[row].size) {
                sheet[horizontal - (row - horizontal)][col] += sheet[row][col]
            }
        }
        repeat(sheet.size - horizontal) { sheet.removeLast() }
    }

    fun foldAlongVertical(vertical: Int = sheet[0].size / 2) {
        for (row in 0 until sheet.size) {
            for (col in sheet[row].size - 1 downTo vertical) {
                sheet[row][vertical - (col - vertical)] += sheet[row][col]
            }
            repeat(sheet[row].size - vertical) { sheet[row].removeLast() }
        }
    }

    override fun toString() = with(StringBuilder()) {
        sheet.forEach { row ->
            append(row.map { num -> when(num) {
                0 -> '.'
                else -> '#'
            } }.joinToString().replace(",", "").replace(" ", ""))
            append("\n")
        }

        toString().trim()
    }

    fun display() = println(with(StringBuilder()) {
        sheet.forEach { row ->
            append(row.map { num -> when(num) {
                0 -> ' '
                else -> '%'
            } }.joinToString().replace(",", ""))
            append("\n")
        }

        toString().trim()
    })
}

enum class InstructionType {
    FOLD_HORIZONTALLY,
    FOLD_VERTICALLY
}

data class Instruction(val type: InstructionType, val position: Int)
