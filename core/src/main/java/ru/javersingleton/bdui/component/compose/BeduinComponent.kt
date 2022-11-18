package ru.javersingleton.bdui.component.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.javersingleton.bdui.core.BeduinController

@Composable
fun BeduinComponent(
    modifier: Modifier = Modifier,
    controller: BeduinController
) {
    val state = controller.root.subscribeAsState()
    BduiComponent(
        modifier = modifier,
        componentStructure = state.value
    )
}