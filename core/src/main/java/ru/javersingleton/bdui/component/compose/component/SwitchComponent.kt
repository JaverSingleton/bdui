package ru.javersingleton.bdui.component.compose.component

import android.util.Log
import androidx.compose.material.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.javersingleton.bdui.component.compose.subscribeAsState
import ru.javersingleton.bdui.component.state.SwitchState
import ru.javersingleton.bdui.core.Value

@Composable
fun SwitchComponent(
    modifier: Modifier = Modifier,
    componentState: Value<SwitchState>
) {
    val state = componentState.subscribeAsState().value
    Log.d("Beduin", "OnComponentRender: componentType=Switch")
    Switch(
        modifier = modifier,
        checked = state.checked,
        enabled = state.enabled,
        onCheckedChange = { state.onCheckedChange?.invoke(it) }
    )
}
