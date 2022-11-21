package ru.javersingleton.bdui.component.compose

import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import ru.javersingleton.bdui.component.compose.component.*
import ru.javersingleton.bdui.component.state.*
import ru.javersingleton.bdui.core.ReadableValue
import ru.javersingleton.bdui.core.Value
import ru.javersingleton.bdui.core.field.ComponentStructure

@Suppress("UNCHECKED_CAST")
@Composable
fun BduiComponent(
    modifier: Modifier = Modifier,
    componentStructure: ComponentStructure
) {
    Log.d("Beduin", "OnComponentRecomposition: componentType=${componentStructure.componentType}")
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
        "Button" -> ButtonComponent(
            modifier = modifier,
            componentState = componentStructure.value as Value<ButtonState>,
        )
        "Image" -> ImageComponent(
            modifier = modifier,
            componentState = componentStructure.value as Value<ImageState>,
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