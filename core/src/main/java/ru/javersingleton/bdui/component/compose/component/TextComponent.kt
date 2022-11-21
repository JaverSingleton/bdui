package ru.javersingleton.bdui.component.compose.component

import android.util.Log
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import ru.javersingleton.bdui.component.compose.subscribeAsState
import ru.javersingleton.bdui.component.state.TextState
import ru.javersingleton.bdui.core.Value

@Composable
fun TextComponent(
    modifier: Modifier = Modifier,
    componentState: Value<TextState>
) {
    val state = componentState.subscribeAsState().value
    Log.d("Beduin", "OnComponentRender: componentType=Text")
    Text(
        modifier = modifier,
        text = state.text,
        textAlign = toTextAlign(state.textAlign),
        fontSize = state.textSize.sp
    )
}

private fun toTextAlign(value: String) = when(value) {
    "Start" -> TextAlign.Start
    "End" -> TextAlign.End
    "Center" -> TextAlign.Center
    "Justify" -> TextAlign.Justify
    "Left" -> TextAlign.Left
    "Right" -> TextAlign.Right
    else -> throw IllegalArgumentException("Invalid text align $value")
}