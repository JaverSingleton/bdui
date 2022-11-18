package ru.javersingleton.bdui.component.compose

import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import ru.javersingleton.bdui.component.compose.component.BoxComponent
import ru.javersingleton.bdui.component.compose.component.ColumnComponent
import ru.javersingleton.bdui.component.compose.component.MetaComponent
import ru.javersingleton.bdui.component.compose.component.TextComponent
import ru.javersingleton.bdui.component.state.BoxState
import ru.javersingleton.bdui.component.state.ColumnState
import ru.javersingleton.bdui.component.state.MetaState
import ru.javersingleton.bdui.component.state.TextState
import ru.javersingleton.bdui.core.ReadableValue
import ru.javersingleton.bdui.core.Value
import ru.javersingleton.bdui.core.field.ComponentStructure

@Suppress("UNCHECKED_CAST")
@Composable
fun BduiComponent(
    modifier: Modifier = Modifier,
    componentStructure: ComponentStructure
) {
    Log.d("Beduin", "BduiComponent(componentType=${componentStructure.componentType})")
    // TODO Вынести отдельно
    when (componentStructure.componentType) {
        "Box" -> BoxComponent(
            modifier = modifier,
            componentState = componentStructure.value as Value<BoxState>,
        )
        "Column" -> ColumnComponent(
            modifier = modifier,
            componentState = componentStructure.value as Value<ColumnState>,
        )
        "Text" -> TextComponent(
            modifier = modifier,
            componentState = componentStructure.value as Value<TextState>,
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
    val result = remember(readableState) { mutableStateOf(readableState.currentValue) }
    DisposableEffect(key1 = readableState) {
        val subscription = readableState.subscribe { newState ->
            result.value = (newState as ReadableValue<T>).currentValue
        }
        onDispose { subscription.unsubscribe() }
    }

    return result
}