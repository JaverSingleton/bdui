package ru.javersingleton.bdui.component.compose.component

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.javersingleton.bdui.component.compose.BduiComponent
import ru.javersingleton.bdui.component.compose.subscribeAsState
import ru.javersingleton.bdui.component.state.ColumnState
import ru.javersingleton.bdui.core.Value

@Composable
fun ColumnComponent(
    modifier: Modifier = Modifier,
    componentState: Value<ColumnState>
) {
    val state = componentState.subscribeAsState().value
    Log.d("Beduin", "OnComponentRender: componentType=Column")
    Column(
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
private fun ColumnScope.toModifier(params: ColumnState.Child.Params): Modifier {
    var result: Modifier = Modifier
    result = when {
        params.width == "fillMaxWidth" -> result.fillMaxWidth()
        params.width == "wrapContentWidth" -> result.wrapContentWidth()
        params.width.toIntOrNull() != null -> result.width(params.width.toInt().dp)
        params.width.isEmpty() -> result
        else -> throw IllegalArgumentException()
    }
    result = when {
        params.height == "fillMaxHeight" -> result.fillMaxHeight()
        params.height == "wrapContentHeight" -> result.wrapContentHeight()
        params.height.toIntOrNull() != null -> result.height(params.height.toInt().dp)
        params.height.isEmpty() -> result
        else -> throw IllegalArgumentException()
    }
    return result
}