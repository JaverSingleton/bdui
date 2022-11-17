package ru.javersingleton.bdui.component.compose.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.javersingleton.bdui.component.compose.BduiComponent
import ru.javersingleton.bdui.component.compose.subscribeAsState
import ru.javersingleton.bdui.component.state.MetaState
import ru.javersingleton.bdui.core.Value

@Composable
fun MetaComponent(
    modifier: Modifier = Modifier,
    componentState: Value<MetaState>
) {
    val state = componentState.subscribeAsState().value
    BduiComponent(
        modifier = modifier,
        componentStructure = state.childComponent,
    )
}