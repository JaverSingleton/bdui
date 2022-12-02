package ru.javersingleton.bdui.component.button.compose

import android.util.Log
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.javersingleton.bdui.render.compose.subscribeAsState
import ru.javersingleton.bdui.engine.core.Value
import ru.javersingleton.bdui.render.compose.ComponentRender

object ButtonComponent: ComponentRender<ButtonState>(ButtonStateFactory) {

    override val type: String = "Button"

    @Composable
    override fun Render(
        modifier: Modifier,
        stateValue: Value<ButtonState>
    ) {
        val state = stateValue.subscribeAsState().value
        Log.d("Beduin", "OnComponentRender: componentType=Button")
        Button(
            onClick = { state.onClick?.invoke() },
            modifier = modifier
        ) {
            Text(text = state.text)
        }
    }

}