package day2

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