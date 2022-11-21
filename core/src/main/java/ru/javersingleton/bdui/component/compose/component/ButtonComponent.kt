package ru.javersingleton.bdui.component.compose.component

import android.util.Log
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.javersingleton.bdui.component.compose.subscribeAsState
import ru.javersingleton.bdui.component.state.ButtonState
import ru.javersingleton.bdui.core.Value

@Composable
fun ButtonComponent(
    modifier: Modifier,
    componentState: Value<ButtonState>
) {
    val state = componentState.subscribeAsState().value
    Log.d("Beduin", "OnComponentRender: componentType=Button")
    Button(
        onClick = { /*TODO*/ },
        modifier = modifier
    ) {
        Text(text = state.text)
    }
}