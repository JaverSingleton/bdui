package ru.javersingleton.bdui.component.compose.component

import android.util.Log
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.javersingleton.bdui.component.compose.subscribeAsState
import ru.javersingleton.bdui.component.state.InputState
import ru.javersingleton.bdui.core.Value

@Composable
fun InputComponent(
    modifier: Modifier,
    componentState: Value<InputState>
) {
    val state = componentState.subscribeAsState().value
    Log.d("Beduin", "OnComponentRender: componentType=Input")
    TextField(
        value = state.text,
        onValueChange = { state.onTextChanged?.invoke(it) },
        modifier = modifier
    )
}