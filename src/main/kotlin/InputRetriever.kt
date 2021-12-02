import java.io.File
import java.net.URL

class InputRetriever {

    suspend fun retrieveInput(day: Int, test: Boolean = false) : String {
        val resource = if (test) "day$day/test_input.txt" else "day$day/input.txt"
        val input = javaClass.getResource(resource)

        if (input == null) {
            println("Could not read Day $day input")
        }

        return input!!.readText()
    }
}