package ru.javersingleton.bdui.component.compose.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.javersingleton.bdui.component.compose.BduiComponent
import ru.javersingleton.bdui.component.compose.asState
import ru.javersingleton.bdui.component.state.MetaState
import ru.javersingleton.bdui.core.State

@Composable
fun MetaComponent(
    modifier: Modifier = Modifier,
    componentState: State<MetaState>
) {
    val state = componentState.asState().value
    BduiComponent(
        modifier = modifier,
        componentStructure = state.childComponent,
    )
}