package ru.javersingleton.bdui.component.compose.component

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import ru.javersingleton.bdui.component.compose.subscribeAsState
import ru.javersingleton.bdui.component.state.TextState
import ru.javersingleton.bdui.core.Value

@Composable
fun TextComponent(
    modifier: Modifier = Modifier,
    componentState: Value<TextState>
) {
    val state = componentState.subscribeAsState().value
    Text(
        modifier = modifier,
        text = state.text,
        fontSize = state.textSize.sp
    )
}