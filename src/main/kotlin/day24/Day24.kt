package day24

import BaseDay
import getInputLines
import java.lang.NumberFormatException
import java.util.*

class Day24 : BaseDay(24, "Arithmetic Logic Unit") {
    override suspend fun partOne(input: String) = solveDay24(input) {
        0L
    }

    override suspend fun partTwo(input: String): Any {
        TODO("Not yet implemented")
    }

    private fun solveDay24(input: String, solver: (List<Instruction>) -> Long) = input.getInputLines().parseInstructions().let(solver)
}

fun List<String>.parseInstructions() = map { it.parseInstruction() }
fun String.parseInstruction() = split(" ").let {
    when {
        it[0] == "inp" -> InputInstruction(it[1])
        it[0] == "add" -> AddInstruction(it[1], it[2].parseValue())
        it[0] == "mod" -> ModInstruction(it[1], it[2].parseValue())
        it[0] == "div" -> DivideInstruction(it[1], it[2].parseValue())
        it[0] == "mul" -> MultiplyInstruction(it[1], it[2].parseValue())
        it[0] == "eql" -> EqualsInstruction(it[1], it[2].parseValue())
        else -> throw IllegalArgumentException("Unrecognized instruction: $this")
    }
}
fun String.parseValue() = try {
    LiteralValue(toLong())
} catch (e: NumberFormatException) {
    VariableValue(this)
}

sealed class Value {
    abstract fun getValue(memory: Map<String, Long>): Long
}
class LiteralValue(val value: Long) : Value() {
    override fun getValue(memory: Map<String, Long>) = value
}
class VariableValue(private val variableName: String) : Value() {
    override fun getValue(memory: Map<String, Long>) = memory[variableName]!!
}

sealed class Instruction(val thisVariable: String, val value: Value?) {
    abstract fun execute(memory: MutableMap<String, Long>, input: Queue<Long>)
}
class InputInstruction(thisVariable: String) : Instruction(thisVariable, null) {
    override fun execute(memory: MutableMap<String, Long>, input: Queue<Long>) {
        memory[thisVariable] = input.remove()
    }
}
sealed class ArithmeticInstruction(thisVariable: String, value: Value) : Instruction(thisVariable, value) {
    override fun execute(memory: MutableMap<String, Long>, input: Queue<Long>) {
        require(thisVariable in memory) { "Trying to perform arithmetic on variable $thisVariable not in memory!" }
        memory[thisVariable] = applyArithmetic(memory[thisVariable]!!, value!!.getValue(memory))
    }
    abstract fun applyArithmetic(firstVal: Long, secondVal: Long): Long
}
class AddInstruction(thisVariable: String, value: Value) : ArithmeticInstruction(thisVariable, value) {
    override fun applyArithmetic(firstVal: Long, secondVal: Long) = firstVal + secondVal
}
class MultiplyInstruction(thisVariable: String, value: Value) : ArithmeticInstruction(thisVariable, value) {
    override fun applyArithmetic(firstVal: Long, secondVal: Long) = firstVal * secondVal
}
class DivideInstruction(thisVariable: String, value: Value) : ArithmeticInstruction(thisVariable, value) {
    override fun applyArithmetic(firstVal: Long, secondVal: Long) = firstVal / secondVal
}
class ModInstruction(thisVariable: String, value: Value) : ArithmeticInstruction(thisVariable, value) {
    override fun applyArithmetic(firstVal: Long, secondVal: Long) = firstVal % secondVal
}
class EqualsInstruction(thisVariable: String, value: Value) : ArithmeticInstruction(thisVariable, value) {
    override fun applyArithmetic(firstVal: Long, secondVal: Long) = if (firstVal == secondVal) 1L else 0L
}

class Program(private val params: Queue<Long>) {
    private val memory = mutableMapOf<String, Long>().apply {
        put("w", 0L)
        put("x", 0L)
        put("y", 0L)
        put("z", 0L)
    }
    
    fun run(instructions: Collection<Instruction>) = instructions.forEach { it.execute(memory, params) }.let { memory["z"] == 0L }
}