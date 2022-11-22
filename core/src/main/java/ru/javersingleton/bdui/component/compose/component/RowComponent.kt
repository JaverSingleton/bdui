package ru.javersingleton.bdui.component.compose.component

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.javersingleton.bdui.component.compose.BduiComponent
import ru.javersingleton.bdui.component.compose.subscribeAsState
import ru.javersingleton.bdui.component.state.RowState
import ru.javersingleton.bdui.core.Value

@Composable
fun RowComponent(
    modifier: Modifier = Modifier,
    componentState: Value<RowState>
) {
    val state = componentState.subscribeAsState().value
    Log.d("Beduin", "OnComponentRender: componentType=Row")
    Row(
        modifier = modifier
    ) {
        state.children.forEach { child ->
            BduiComponent(
                componentStructure = child.component,
                modifier = toModifier(child.params),
            )
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