package ru.javersingleton.bdui.component.compose.component

import android.annotation.SuppressLint
import android.graphics.Color.parseColor
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.javersingleton.bdui.component.compose.BduiComponent
import ru.javersingleton.bdui.component.compose.subscribeAsState
import ru.javersingleton.bdui.component.state.BoxState
import ru.javersingleton.bdui.core.Value

@Composable
fun BoxComponent(
    modifier: Modifier = Modifier,
    componentState: Value<BoxState>
) {
    val state = componentState.subscribeAsState().value
    Log.d("Beduin", "OnComponentRender: componentType=Box")
    Box(
        modifier = Modifier
            .background(color = Color(parseColor(state.backgroundColor)))
            .then(
                if (state.onClick != null) {
                    Modifier.clickable { state.onClick.invoke() }
                } else {
                    Modifier
                }
            )
            .then(modifier)
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
private fun BoxScope.toModifier(params: BoxState.Child.Params): Modifier {
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
    if (params.alignment.isNotEmpty()) {
        result = result.align(
            when (params.alignment) {
                "TopStart" -> Alignment.TopStart
                "TopCenter" -> Alignment.TopCenter
                "TopEnd" -> Alignment.TopEnd
                "CenterStart" -> Alignment.CenterStart
                "Center" -> Alignment.Center
                "CenterEnd" -> Alignment.CenterEnd
                "BottomStart" -> Alignment.BottomStart
                "BottomCenter" -> Alignment.BottomCenter
                "BottomEnd" -> Alignment.BottomEnd
                else -> throw IllegalArgumentException()
            }
        )
    }
    return result
}