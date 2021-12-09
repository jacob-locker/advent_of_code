import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.coroutines.*
import java.awt.Dimension

fun main() = application {
    Window(title = "Advent of Code 2021", resizable = false, onCloseRequest = ::exitApplication) {
        this.window.size = Dimension(928, 572)
        App(retrieveImplementedDays())
    }
}

var executeDayJob: Job? = null

@Composable
@Preview
fun App(dayObjs: Map<Int, BaseDay>) {
    MaterialTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val days = arrayOf("Su", "Mo", "Tu", "We", "Th", "Fr", "Sa")
                for (day in days) {
                    Text(day, style = MaterialTheme.typography.h6, modifier = Modifier.width(128.dp))
                }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                for (day in 28..30) {
                    DayLayout(dayObjs[day], day, isEmpty = true)
                }
                for (day in 1..4) {
                    DayLayout(dayObjs[day], day, isEmpty = false)
                }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                for (day in 5..11) {
                    DayLayout(dayObjs[day], day, isEmpty = false)
                }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                for (day in 12..18) {
                    DayLayout(dayObjs[day], day, isEmpty = false)
                }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                for (day in 19..25) {
                    DayLayout(dayObjs[day], day, isEmpty = false)
                }
            }
        }
    }
}

@Composable
fun DayLayout(day: BaseDay?, dayNumber: Int, isEmpty: Boolean = false) {
    val modifier = Modifier.size(128.dp).padding(top = 2.dp, bottom = 2.dp)
    val coroutineScope = rememberCoroutineScope()
    if (day != null) {
        Button(onClick = {
            executeDay(coroutineScope, day)
        }, modifier = modifier) {
            Text("${day.number}")
        }
    } else if (isEmpty) {
        Button(onClick = {}, enabled = false, modifier = modifier) {
            Text("$dayNumber")
        }
    } else {
        OutlinedButton(onClick = {}, modifier = modifier) {
            Text("$dayNumber")
        }
    }
}

fun executeDay(coroutineScope: CoroutineScope, day: BaseDay) {
    executeDayJob?.cancel()
    executeDayJob = coroutineScope.launch(Dispatchers.Default) {
        try {
            printDay(day.number, day.title) {
                printPart { day.partOne(it) }
                printPart { day.partTwo(it) }
            }
        } catch (e: Exception) {}
    }
}

class DayScope {
    val parts: List<suspend ((String) -> Any)>
        get() = _parts

    private val _parts = mutableListOf<suspend ((String) -> Any)>()

    fun printPart(partMethod: suspend ((String) -> Any)) {
        _parts.add(partMethod)
    }
}

suspend fun printDay(dayNumber: Int, dayTitle: String, dayScopeAction: DayScope.() -> Unit) = coroutineScope {
    println("-- Day $dayNumber: $dayTitle --")
    val dayInput = DayRetriever().retrieveInput(dayNumber)

    println("Input: ${dayInput.getInputLines()}")

    val dayScope = DayScope()
    dayScopeAction.invoke(dayScope)

    dayScope.parts.forEachIndexed { index, partMethod ->
        println("Part ${index + 1} Output: ${partMethod(dayInput)}")
    }
    println()
}