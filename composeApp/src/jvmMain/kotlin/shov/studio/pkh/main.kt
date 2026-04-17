package shov.studio.pkh

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "PKH",
    ) {
        App()
    }
}