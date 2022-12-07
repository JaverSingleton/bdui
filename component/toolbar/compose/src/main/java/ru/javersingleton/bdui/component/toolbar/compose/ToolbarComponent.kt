package ru.javersingleton.bdui.component.toolbar.compose

import android.util.Log
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.javersingleton.bdui.engine.core.Value
import ru.javersingleton.bdui.render.compose.ComponentRender
import ru.javersingleton.bdui.render.compose.subscribeAsState

object ToolbarComponent : ComponentRender<ToolbarState>(ToolbarStateFactory) {

    override val key: String = "Toolbar"

    @Composable
    override fun Render(
        modifier: Modifier,
        stateValue: Value<ToolbarState>
    ) {
        val state = stateValue.subscribeAsState().value
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

}