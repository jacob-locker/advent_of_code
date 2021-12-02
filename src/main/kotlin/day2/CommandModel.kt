package day2
enum class CommandDirection {
    FORWARD,
    DOWN,
    UP
}

data class Command(val direction: CommandDirection, val magnitude: Int)

fun String.toCommand(): Command {
    val splitCommand = split(" ")
    val direction = when (splitCommand[0]) {
        "forward" -> CommandDirection.FORWARD
        "down" -> CommandDirection.DOWN
        "up" -> CommandDirection.UP
        else -> throw IllegalArgumentException("Command direction not recognized ${splitCommand[0]}")
    }
    val magnitude = splitCommand[1].toInt()

    return Command(direction, magnitude)
}