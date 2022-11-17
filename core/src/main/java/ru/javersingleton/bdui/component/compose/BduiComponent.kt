package ru.javersingleton.bdui.component.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import ru.javersingleton.bdui.component.compose.component.BoxComponent
import ru.javersingleton.bdui.component.compose.component.MetaComponent
import ru.javersingleton.bdui.component.state.BoxState
import ru.javersingleton.bdui.component.state.MetaState
import ru.javersingleton.bdui.core.ReadableState
import ru.javersingleton.bdui.core.State
import ru.javersingleton.bdui.core.field.ComponentStructure

@Suppress("UNCHECKED_CAST")
@Composable
fun BduiComponent(
    modifier: Modifier = Modifier,
    componentStructure: ComponentStructure
) {
    // TODO Вынести отдельно
    when (componentStructure.componentType) {
        "Box" -> BoxComponent(
            modifier = modifier,
            componentState = componentStructure.state as State<BoxState>,
        )
        else -> MetaComponent(
            modifier = modifier,
            componentState = componentStructure.state as State<MetaState>,
        )
    }
}

@Suppress("UNCHECKED_CAST")
@Composable
fun <T> State<T>.asState(): androidx.compose.runtime.State<T> {
    val readableState = this as ReadableState<T>
    val result = remember { mutableStateOf(readableState.currentValue) }
    DisposableEffect(key1 = readableState) {
        val subscription = readableState.subscribe { newState ->
            result.value = (newState as ReadableState<T>).currentValue
        }
        onDispose { subscription.unsubscribe() }
    }

    return result
}