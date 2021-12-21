package day11

import ColorScheme
import DayVisualization
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import copy
import getInputLines
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import ui.ColorSchemeSelection
import ui.GridLayout

class Day11Visualization : DayVisualization(11) {

    private val gridFlow = MutableStateFlow(OctopusGrid(Array(10) { IntArray(10) }))

    override fun launchPartOneJob(input: String, coroutineScope: CoroutineScope) =
        coroutineScope.launch(Dispatchers.Default) {
            var octopusGrid = input.getInputLines().parseOctopusGrid()
            repeat(100) {
                octopusGrid = OctopusGrid(octopusGrid.values.copy(), octopusGrid.numberOfFlashes)
                octopusGrid.step()
                gridFlow.value = octopusGrid
                _partOneOutputFlow.value = "Number of Flashes: ${octopusGrid.numberOfFlashes}"
                delay(500)
            }
        }

    override fun launchPartTwoJob(input: String, coroutineScope: CoroutineScope) =
        coroutineScope.launch(Dispatchers.Default) {
            var octopusGrid = input.getInputLines().parseOctopusGrid()
            var count = 1
            while (!octopusGrid.allOctopusesAreFlashing()) {
                octopusGrid = OctopusGrid(octopusGrid.values.copy(), octopusGrid.numberOfFlashes)
                octopusGrid.step()
                gridFlow.value = octopusGrid
                _partTwoOutputFlow.value = "Number of Steps: ${count++}"
                delay(200)
            }
        }

    @Composable
    override fun VisualizePartOne(modifier: Modifier) {
        val octopusGrid = gridFlow.collectAsState(OctopusGrid(Array(10) { IntArray(10) }))
        OctopusGridLayout(octopusGrid.value, modifier)

    }

    @Composable
    override fun VisualizePartTwo(modifier: Modifier) {
        val octopusGrid = gridFlow.collectAsState(OctopusGrid(Array(10) { IntArray(10) }))
        OctopusGridLayout(octopusGrid.value, modifier)
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun OctopusGridLayout(octopusGrid: OctopusGrid, modifier: Modifier) {
        Column {
            val colorSchemeState = remember { mutableStateOf(ColorScheme.DEFAULT) }
            ColorSchemeSelection {
                colorSchemeState.value = it
            }
            GridLayout(
                octopusGrid.size, octopusGrid.size,
                modifier = modifier.padding(top = 8.dp).border(4.dp, color = colorSchemeState.value.colors[0])
            ) { row, col ->
                OctopusItem(octopusGrid.values[row][col], colorSchemeState.value.colors)
            }
        }
    }

    @Composable
    fun OctopusItem(value: Int, colorScheme: List<Color>) {
        val intensity = if (value == 0) 1F else 1F - (value / 9F)
        val color = when {
            value == 0 -> {
                colorScheme[0]
            }
            value <= 2 -> {
                colorScheme[1]
            }
            value <= 4 -> {
                colorScheme[2]
            }
            value <= 6 -> {
                colorScheme[3]
            }
            else -> {
                colorScheme[4]
            }
        }
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = color,
            elevation = (16 * intensity).dp,
            modifier = Modifier.size(80.dp).graphicsLayer {
                scaleX = intensity
                scaleY = intensity
                translationX = -20 * (1F - intensity)
                translationY = -20 * (1F - intensity)
            }
                .alpha(intensity).zIndex(intensity)) {}
    }
}