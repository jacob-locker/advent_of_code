class InputRetriever {

    fun retrieveInput(day: Int, test: Boolean = false) : String {
        val resource = if (test) "day$day/test_input.txt" else "day$day/input.txt"
        val input = javaClass.getResource(resource)

        if (input == null) {
            println("Could not find or read file $resource")
        }

        return input!!.readText()
    }
}