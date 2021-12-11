package day10

import BaseDay
import getInputLines
import java.util.*

class Day10 : BaseDay(10, "Syntax Scoring") {
    override suspend fun partOne(input: String) = solveDay10(input) { lines ->
        lines.sumOf { with(SyntaxScorer()) { scoreCorruptedLine(it) } }
    }

    override suspend fun partTwo(input: String) = solveDay10(input) { lines ->
        lines.mapNotNull { with(SyntaxScorer()) { scoreIncompleteLine(it) } }
            .sorted()
            .let { it[it.size / 2] }
    }

    private fun solveDay10(input: String, solver: (List<String>) -> Long) = input.getInputLines().let(solver)
}

class SyntaxScorer {

    fun scoreCorruptedLine(line: String): Long {
        val openCharStack = Stack<Char>()
        line.forEach { char ->
            if (char.isOpen()) {
                openCharStack.push(char)
            } else if (char.isClosed() && openCharStack.pop().toClosed() != char) {
                return CLOSED_MAP[char]?.errorScore!!
            }
        }

        return 0L
    }

    fun scoreIncompleteLine(line: String): Long? {
        if (scoreCorruptedLine(line) != 0L) {
            return null
        }

        val openCharStack = Stack<Char>()
        line.forEach { char ->
            if (char.isOpen()) {
                openCharStack.push(char)
            } else if (char.isClosed()) {
                openCharStack.pop()
            }
        }

        var score = 0L
        while (openCharStack.isNotEmpty()) {
            score = 5L * score + CLOSED_MAP[openCharStack.pop().toClosed()]?.incompleteScore!!
        }

        return score
    }
}

val CLOSED_MAP = mapOf(
    ')' to Score(3L, 1L),
    ']' to Score(57L, 2L),
    '}' to Score(1197L, 3L),
    '>' to Score(25137L, 4L)
)

data class Score(val errorScore: Long, val incompleteScore: Long)

fun Char.isOpen() = this == '(' || this == '{' || this == '<' || this == '['

fun Char.toClosed() = when (this) {
    '(' -> ')'
    '{' -> '}'
    '<' -> '>'
    '[' -> ']'
    else -> ')'
}

fun Char.isClosed() = CLOSED_MAP.containsKey(this)