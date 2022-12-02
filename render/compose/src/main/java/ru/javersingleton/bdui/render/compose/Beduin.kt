package ru.javersingleton.bdui.render.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.javersingleton.bdui.engine.BeduinController

@Composable
fun Beduin(
    modifier: Modifier = Modifier,
    controller: BeduinController
) {
    val state = controller.root.subscribeAsState()
    Component(
        modifier = modifier,
        data = state.value
    )
}