import org.reflections.Reflections
import org.reflections.util.ConfigurationBuilder
import java.net.URL
import java.util.stream.Collectors.toList


fun String.getInputLines() = split(System.lineSeparator())

fun String.getInputBlocks() = split(System.lineSeparator() + System.lineSeparator())

fun retrieveImplementedDays() : Map<Int, BaseDay> {
    val reflections = Reflections(IntRange(1, 25).map { "day$it" })
    val annotated: Set<Class<*>> = reflections.getSubTypesOf(BaseDay::class.java)

    return mutableMapOf<Int, BaseDay>().apply {
        for (day in annotated) {
            val dayObj = day.getConstructor().newInstance() as BaseDay
            put(dayObj.number, dayObj)
        }
    }
}

fun retrieveVisualizations() : Map<Int, DayVisualization> {
    val reflections = Reflections(IntRange(1, 25).map { "day$it" })
    val annotated: Set<Class<*>> = reflections.getSubTypesOf(DayVisualization::class.java)

    return mutableMapOf<Int, DayVisualization>().apply {
        for (day in annotated) {
            val dayObj = day.getConstructor().newInstance() as DayVisualization
            put(dayObj.number, dayObj)
        }
    }
}

fun findPackageNamesStartingWith(prefix: String): List<URL> {
    val packages = Package.getPackages()
    return packages.map { obj: Package -> obj.name }
        .filter { n -> n.startsWith(prefix) }
        .map { URL(it) }
}

fun Array<IntArray>.copy() = Array(size) { get(it).clone() }
