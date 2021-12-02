fun String.getInputLines() = split("\r\n")

suspend fun retrieveInput(day: Int, test: Boolean = false) : String {
    val inputRetriever = InputRetriever()
    return inputRetriever.retrieveInput(day, test)
}