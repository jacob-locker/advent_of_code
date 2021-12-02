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

enum class CommandDirection {
    FORWARD,
    DOWN,
    UP
}

data class Command(val direction: CommandDirection, val magnitude: Int)

class Submarine(
    private var _depth: Int = 0,
    private var _horizontalPosition: Int = 0,
    private var _aim: Int = 0
) {
    val depth
    get() = _depth

    val horizontalPosition
    get() = _horizontalPosition

    val aim
    get() = _aim

    fun dive(command: Command) {
        when (command.direction) {
            CommandDirection.FORWARD -> _horizontalPosition += command.magnitude
            CommandDirection.DOWN -> _depth += command.magnitude
            CommandDirection.UP -> _depth -= command.magnitude
        }
    }

    fun diveWithAim(command: Command) {
        when (command.direction) {
            CommandDirection.FORWARD -> {
                _horizontalPosition += command.magnitude
                _depth += (_aim * command.magnitude)
            }
            CommandDirection.DOWN -> {
                _aim += command.magnitude
            }
            CommandDirection.UP -> {
                _aim -= command.magnitude
            }
        }
    }
}

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