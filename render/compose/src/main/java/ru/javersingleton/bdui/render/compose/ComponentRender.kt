package ru.javersingleton.bdui.render.compose

import android.annotation.SuppressLint
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import ru.javersingleton.bdui.engine.ComponentStateFactory
import ru.javersingleton.bdui.engine.core.Value
import ru.javersingleton.bdui.engine.register.ByTypeStrategy

abstract class ComponentRender<T>(
    val stateFactory: ComponentStateFactory<T>
) : ByTypeStrategy.Element<String> {

    @SuppressLint("ComposableNaming")
    @Composable
    operator fun invoke(
        modifier: Modifier,
        stateValue: Value<*>,
    ) {
        @Suppress("UNCHECKED_CAST")
        Render(
            modifier = modifier,
            stateValue = stateValue as Value<T>
        )
    }

    @Composable
    abstract fun Render(
        modifier: Modifier,
        stateValue: Value<T>,
    )

    @Suppress("UNCHECKED_CAST")
    @Composable
    fun <T> Value<T>.subscribeAsState(): State<T> = subscribeAsState(type)

}