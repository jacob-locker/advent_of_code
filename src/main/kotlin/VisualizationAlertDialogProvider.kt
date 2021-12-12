//import androidx.compose.foundation.gestures.detectTapGestures
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.material.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.ExperimentalComposeUiApi
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.input.key.Key
//import androidx.compose.ui.input.key.key
//import androidx.compose.ui.input.pointer.pointerInput
//import androidx.compose.ui.unit.*
//import androidx.compose.ui.window.Popup
//import androidx.compose.ui.window.PopupPositionProvider
//
//@OptIn(ExperimentalMaterialApi::class, androidx.compose.ui.ExperimentalComposeUiApi::class)
//object VisualizationAlertDialogProvider : AlertDialogProvider {
//
//    override fun AlertDialog(onDismissRequest: () -> Unit, content: () -> Unit) {
//        Popup(
//            popupPositionProvider = object : PopupPositionProvider {
//                override fun calculatePosition(
//                    anchorBounds: IntRect,
//                    windowSize: IntSize,
//                    layoutDirection: LayoutDirection,
//                    popupContentSize: IntSize
//                ): IntOffset = IntOffset.Zero
//            },
//            focusable = true,
//            onDismissRequest = onDismissRequest,
//            onKeyEvent = {
//                if (it.key == Key.Escape) {
//                    onDismissRequest()
//                    true
//                } else {
//                    false
//                }
//            },
//        ) {
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .pointerInput(onDismissRequest) {
//                        detectTapGestures(onPress = { onDismissRequest() })
//                    },
//                contentAlignment = Alignment.Center
//            ) {
//                Surface(elevation = 24.dp) {
//                    content()
//                    Button(onClick = { onDismissRequest.invoke() }) { Text("Dimiss") }
//                }
//            }
//        }
//    }
//}