package ru.javersingleton.bdui.component.compose.component

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
            .background(color = Color(state.backgroundColor))
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

@SuppressLint("ModifierFactoryExtensionFunction")
private fun BoxScope.toModifier(params: BoxState.Child.Params): Modifier = Modifier
    .align(
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