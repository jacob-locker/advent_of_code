package day2

import getInputLines

fun parseDay2Input(input: String): List<Command> {
    val lines = input.getInputLines()

    return lines.map { it.toCommand() }
}

fun dive(input: String) = dive(parseDay2Input(input))
fun dive(commands: List<Command>) =
    with(Submarine()) {
        commands.forEach { dive(it) }
        depth * horizontalPosition
    }

fun diveWithAim(input: String) = diveWithAim(parseDay2Input(input))
fun diveWithAim(commands: List<Command>) =
    with(Submarine()) {
        commands.forEach { diveWithAim(it) }
        depth * horizontalPosition
    }