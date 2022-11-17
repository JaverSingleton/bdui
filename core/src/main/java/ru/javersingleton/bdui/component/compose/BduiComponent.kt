package ru.javersingleton.bdui.component.compose

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import ru.javersingleton.bdui.component.compose.component.BoxComponent
import ru.javersingleton.bdui.component.compose.component.MetaComponent
import ru.javersingleton.bdui.component.state.BoxState
import ru.javersingleton.bdui.component.state.MetaState
import ru.javersingleton.bdui.core.ReadableValue
import ru.javersingleton.bdui.core.Value
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
            componentState = componentStructure.value as Value<BoxState>,
        )
        else -> MetaComponent(
            modifier = modifier,
            componentState = componentStructure.value as Value<MetaState>,
        )
    }
}

@Suppress("UNCHECKED_CAST")
@Composable
fun <T> Value<T>.subscribeAsState(): State<T> {
    val readableState = this as ReadableValue<T>
    val result = remember { mutableStateOf(readableState.currentValue) }
    DisposableEffect(key1 = readableState) {
        val subscription = readableState.subscribe { newState ->
            result.value = (newState as ReadableValue<T>).currentValue
        }
        onDispose { subscription.unsubscribe() }
    }

    return result
}