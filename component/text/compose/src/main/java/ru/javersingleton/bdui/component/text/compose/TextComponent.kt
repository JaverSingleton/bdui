package ru.javersingleton.bdui.component.text.compose

import android.util.Log
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import ru.javersingleton.bdui.engine.core.Value
import ru.javersingleton.bdui.render.compose.ComponentRender
import ru.javersingleton.bdui.render.compose.subscribeAsState

object TextComponent : ComponentRender<TextState>(TextStateFactory) {

    override val type: String = "Text"

    @Composable
    override fun Render(
        modifier: Modifier,
        stateValue: Value<TextState>
    ) {
        val state = stateValue.subscribeAsState().value
        Log.d("Beduin", "OnComponentRender: componentType=Text")
        Text(
            modifier = modifier,
            text = state.text,
            textAlign = toTextAlign(state.textAlign),
            fontSize = state.textSize.sp
        )
    }

}

private fun toTextAlign(value: String) = when (value) {
    "Start" -> TextAlign.Start
    "End" -> TextAlign.End
    "Center" -> TextAlign.Center
    "Justify" -> TextAlign.Justify
    "Left" -> TextAlign.Left
    "Right" -> TextAlign.Right
    else -> throw IllegalArgumentException("Invalid text align $value")
}