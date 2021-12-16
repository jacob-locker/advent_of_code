package ui

import ColorScheme
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@Composable
fun <T> DropDownList(
    requestToOpen: Boolean = false,
    list: List<T>,
    request: (Boolean) -> Unit,
    selectedItem: (T) -> Unit
) {
    DropdownMenu(
        modifier = Modifier.fillMaxWidth(),
        expanded = requestToOpen,
        onDismissRequest = { request(false) },
    ) {
        list.forEach {
            DropdownMenuItem(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    request(false)
                    selectedItem(it)
                }
            ) {
                Text(it.toString(), modifier = Modifier.wrapContentWidth())
            }
        }
    }
}
@Composable
fun ColorSchemeSelection(initialSelection: ColorScheme = ColorScheme.DEFAULT, onItemSelected: (ColorScheme) -> Unit) {
    val colorSchemeList = ColorScheme.values().toList()
    val text = remember { mutableStateOf(initialSelection.toString()) } // initial value
    val isOpen = remember { mutableStateOf(false) } // initial value
    val openCloseOfDropDownList: (Boolean) -> Unit = {
        isOpen.value = it
    }
    val userSelectedItem: (ColorScheme) -> Unit = {
        text.value = it.toString()
        onItemSelected(it)
    }
    Box {
        Column {
            OutlinedTextField(
                value = text.value,
                onValueChange = { text.value = it },
                label = { Text(text = "Color Scheme") }
            )
            DropDownList(
                requestToOpen = isOpen.value,
                list = colorSchemeList,
                openCloseOfDropDownList,
                userSelectedItem
            )
        }
        Spacer(
            modifier = Modifier.matchParentSize().background(Color.Transparent).padding(10.dp)
                .clickable(
                    onClick = { isOpen.value = true }
                )
        )
    }
}

@Composable
fun GridLayout(rows: Int,
               cols: Int,
               modifier: Modifier = Modifier,
               backgroundColor: Color = Color.Black,
               content: @Composable (Int, Int) -> Unit) {
    Surface(
        modifier = modifier,
        color = backgroundColor
    ) {
        Column(verticalArrangement = Arrangement.SpaceEvenly) {
            repeat(rows) { row ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    repeat(cols) { col ->
                        content(row, col)
                    }
                }
            }
        }
    }
}