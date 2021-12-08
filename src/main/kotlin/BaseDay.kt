abstract class BaseDay(val number: Int, val title: String) : Comparable<BaseDay> {
    abstract suspend fun partOne(input: String) : Any
    abstract suspend fun partTwo(input: String) : Any
    override fun compareTo(other: BaseDay) = number.compareTo(other.number)
}