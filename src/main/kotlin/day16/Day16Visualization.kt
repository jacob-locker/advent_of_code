package day16

import DayVisualization
import androidx.compose.foundation.Canvas
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.Typeface
import androidx.compose.ui.text.platform.Font
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.jetbrains.skia.TextLine
import kotlin.math.min

class Day16Visualization : DayVisualization(16) {
    private val packetFlow = MutableStateFlow<Packet?>(null)

    override fun launchPartOneJob(input: String, coroutineScope: CoroutineScope) = coroutineScope.launch(Dispatchers.Default) {
        _partOneOutputFlow.value = "Versions: ${Day16().partOne(input)}"
    }

    override fun launchPartTwoJob(input: String, coroutineScope: CoroutineScope) = coroutineScope.launch(Dispatchers.Default) {
        packetFlow.value = input.hexToBinary().parsePacket().first
    }

    @Composable
    override fun VisualizePartOne(modifier: Modifier) {
        Surface(modifier = modifier) {}
    }

    @Composable
    override fun VisualizePartTwo(modifier: Modifier) {
        val state = packetFlow.collectAsState()
        state.value?.let {
            TreeLayout(it, modifier)
        }
    }

    @Composable
    fun TreeLayout(packet: Packet, modifier: Modifier) {
        Canvas(modifier = modifier) {
            val canvasHeight = size.height
            val canvasWidth = size.width

            val nodeHeight = canvasHeight / packet.height
            val nodeWidth = canvasWidth / packet.width

            drawTree(packet, min(nodeHeight, nodeWidth))
        }
    }

    fun DrawScope.drawTree(packet: Packet, nodeSize: Float) {
        if (packet.level == 0) {
            val paint = Paint().asFrameworkPaint().apply {
                color = Color(0xFF0018A8).toArgb()
            }

            drawIntoCanvas {
                it.nativeCanvas.drawTextLine(TextLine.Companion.make("${packet.value}", null), 0f, 0f, paint)
            }

            drawCircle(color = Color.Red, center = Offset(this.center.x, 8f), radius = nodeSize / 2)
        }
    }
}