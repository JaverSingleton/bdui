package ru.javersingleton.bdui.component.switch.compose

import android.util.Log
import androidx.compose.material.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.javersingleton.bdui.engine.core.Value
import ru.javersingleton.bdui.render.compose.ComponentRender
import ru.javersingleton.bdui.render.compose.subscribeAsState

object SwitchComponent : ComponentRender<SwitchState>(SwitchStateFactory) {

    override val type: String = "Switch"

    @Composable
    override fun Render(
        modifier: Modifier,
        stateValue: Value<SwitchState>
    ) {
        val state = stateValue.subscribeAsState().value
        Log.d("Beduin", "OnComponentRender: componentType=Switch")
        Switch(
            modifier = modifier,
            checked = state.checked,
            enabled = state.enabled,
            onCheckedChange = { state.onCheckedChange?.invoke(it) }
        )
    }

}
