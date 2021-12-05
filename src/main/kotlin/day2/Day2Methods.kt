package day2

import getInputLines

fun solveDay2(input: String, solver: (List<Command>) -> Int) = input.getInputLines().map { it.toCommand() }.let(solver)

fun dive(input: String) = solveDay2(input) { commands ->
    with(Submarine()) {
        commands.forEach { dive(it) }
        depth * horizontalPosition
    }
}

fun diveWithAim(input: String) = solveDay2(input) { commands ->
    with(Submarine()) {
        commands.forEach { diveWithAim(it) }
        depth * horizontalPosition
    }
}
