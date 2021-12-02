import java.io.File
import java.net.URL

class InputRetriever {

    suspend fun retrieveInput(day: Int) : String {
        val input = javaClass.getResource("day$day/input.txt")

        if (input == null) {
            println("Could not read Day $day input")
        }

        return input!!.readText()
    }
}