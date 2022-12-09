package ru.javersingleton.bdui.render.compose

import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import ru.javersingleton.bdui.engine.core.ReadableValue
import ru.javersingleton.bdui.engine.core.Value
import ru.javersingleton.bdui.engine.field.ComponentData

@Composable
fun InnerComponent(
    modifier: Modifier = Modifier,
    data: ComponentData
) {
    Log.d("Beduin", "OnComponentRecomposition: componentType=${data.componentType}")
    val context = LocalBeduinContext.current
    val render = remember(data.componentType) {
        context.inflateComponentRender(data.componentType)
    }
    render(
        modifier.recomposeHighlighter(),
        data.value
    )
}

@Suppress("UNCHECKED_CAST")
@Composable
fun <T> Value<T>.subscribeAsState(name: String = "Noname"): State<T> {
    val readableState = this as ReadableValue<T>
    val result = remember(readableState) { mutableStateOf(readableState.currentValue) }
    DisposableEffect(key1 = readableState) {
        val subscription = readableState.subscribeEndpoint(
            object : ReadableValue.Subscription.EndCallback {
                override fun onInvalidated(reason: String) {
                    Log.d("Beduin-Invalidating", "Component $name invalidated by $reason")
                    result.value = readableState.currentValue
                }
            }
        )
        onDispose { subscription.unsubscribe() }
    }

    return result
}