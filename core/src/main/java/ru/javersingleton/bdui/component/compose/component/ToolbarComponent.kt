package ru.javersingleton.bdui.component.compose.component

import android.util.Log
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.javersingleton.bdui.component.compose.subscribeAsState
import ru.javersingleton.bdui.component.state.ToolbarState
import ru.javersingleton.bdui.core.Value

@Composable
fun ToolbarComponent(
    modifier: Modifier,
    componentState: Value<ToolbarState>
) {
    val state = componentState.subscribeAsState().value
    Log.d("Beduin", "OnComponentRender: componentType=Toolbar")
    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                state.title
            )
        }
    )
}