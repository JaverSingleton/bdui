package ru.javersingleton.bdui.component.input.compose

import android.util.Log
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.javersingleton.bdui.engine.core.Value
import ru.javersingleton.bdui.render.compose.ComponentRender
import ru.javersingleton.bdui.render.compose.subscribeAsState

object InputComponent : ComponentRender<InputState>(InputStateFactory) {

    override val type: String = "Input"

    @Composable
    override fun Render(
        modifier: Modifier,
        stateValue: Value<InputState>
    ) {
        val state = stateValue.subscribeAsState(type).value
        Log.d("Beduin", "OnComponentRender: componentType=Input")
        TextField(
            value = state.text,
            onValueChange = { state.onTextChanged?.invoke(it) },
            modifier = modifier
        )
    }

}