import java.io.File
import java.nio.file.Paths


class DayRetriever {

    suspend fun retrieveInput(day: Int, test: Boolean = false) : String {
        val resource = if (test) "day$day/test_input.txt" else "day$day/input.txt"
        val input = javaClass.getResource(resource)

        if (input == null) {
            println("Could not find or read file $resource")
//            val dirString = "src/main/resources/day$day"
//            val dir = File(File(dirString).absolutePath)
//            if (!dir.exists()) {
//                dir.mkdir()
//            }
//
//            val inputAsString = retrieveInputRemotely(day)
//            File("$dirString/input.txt").writer().use { it.write(inputAsString) }
//            return inputAsString
        }

        return input!!.readText()
    }

//    suspend fun retrieveInputRemotely(day: Int) : String {
//        javaClass.getResource("login.txt")?.readText()?.let {
//            val service = createAocService(it)
//            return service.fetchDayInput(day = day)
//        }
//
//        return ""
//    }
}