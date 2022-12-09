package ru.javersingleton.bdui.component.row.compose

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.javersingleton.bdui.engine.core.Value
import ru.javersingleton.bdui.render.compose.ComponentRender
import ru.javersingleton.bdui.render.compose.InnerComponent
import ru.javersingleton.bdui.render.compose.subscribeAsState

object RowComponent : ComponentRender<RowState>(RowStateFactory) {

    override val key: String = "Row"

    @Composable
    override fun Render(
        modifier: Modifier,
        stateValue: Value<RowState>
    ) {
        val state = stateValue.subscribeAsState().value
        Log.d("Beduin", "OnComponentRender: componentType=Row")
        Row(
            modifier = modifier
        ) {
            state.children.forEach { child ->
                InnerComponent(
                    data = child.component,
                    modifier = toModifier(child.params),
                )
            }
        }
    }

}

@Composable
@SuppressLint("ModifierFactoryExtensionFunction", "ComposableModifierFactory")
private fun RowScope.toModifier(params: RowState.Child.Params): Modifier {
    var result: Modifier = Modifier
    result = when {
        params.width == "fillMax" -> result.fillMaxWidth()
        params.width == "wrapContent" -> result.wrapContentWidth()
        params.width.toIntOrNull() != null -> result.width(params.width.toInt().dp)
        params.width.isEmpty() -> result
        else -> throw IllegalArgumentException()
    }
    result = when {
        params.height == "fillMax" -> result.fillMaxHeight()
        params.height == "wrapContent" -> result.wrapContentHeight()
        params.height.toIntOrNull() != null -> result.height(params.height.toInt().dp)
        params.height.isEmpty() -> result
        else -> throw IllegalArgumentException()
    }
    if (params.padding != null) {
        result = result.padding(
            params.padding.run {
                PaddingValues(
                    start = start.dp,
                    end = end.dp,
                    top = top.dp,
                    bottom = bottom.dp,
                )
            }
        )
    }
    return result
}