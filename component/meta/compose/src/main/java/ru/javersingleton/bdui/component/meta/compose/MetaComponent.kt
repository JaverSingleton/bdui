package ru.javersingleton.bdui.component.meta.compose

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.javersingleton.bdui.engine.core.Value
import ru.javersingleton.bdui.render.compose.ComponentRender
import ru.javersingleton.bdui.render.compose.InnerComponent
import ru.javersingleton.bdui.render.compose.subscribeAsState

object MetaComponent: ComponentRender<MetaState>(MetaStateFactory) {

    override val type: String = "Meta"

    @Composable
    override fun Render(
        modifier: Modifier,
        stateValue: Value<MetaState>
    ) {
        val state = stateValue.subscribeAsState().value
        Log.d("Beduin", "OnComponentRender: componentType=Meta")
        InnerComponent(
            modifier = modifier,
            data = state.childComponent,
        )
    }

}