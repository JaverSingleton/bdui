package ru.javersingleton.bdui.component.lazy_column.compose

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.javersingleton.bdui.engine.core.Value
import ru.javersingleton.bdui.render.compose.InnerComponent
import ru.javersingleton.bdui.render.compose.ComponentRender
import ru.javersingleton.bdui.render.compose.subscribeAsState

object LazyColumnComponent: ComponentRender<LazyColumnState>(LazyColumnStateFactory) {

    override val type: String = "LazyColumn"

    @Composable
    override fun Render(
        modifier: Modifier,
        stateValue: Value<LazyColumnState>
    ) {
        val state = stateValue.subscribeAsState().value
        Log.d("Beduin", "OnComponentRender: componentType=LazyColumn")
        LazyColumn(
            modifier = modifier
        ) {
            items(
                items = state.children,
                key = { item ->
                    item.component.id
                },
                contentType = { item ->
                    item.component.componentType
                },
                itemContent = { item ->
                    InnerComponent(
                        data = item.component,
                    )
                }
            )
        }
    }

}

@Composable
@SuppressLint("ModifierFactoryExtensionFunction", "ComposableModifierFactory")
private fun LazyItemScope.toModifier(params: LazyColumnState.Child.Params): Modifier {
    var result: Modifier = Modifier
    result = when {
        params.width == "fillMax" -> result.fillMaxWidth()
        params.width == "wrapContent" -> result.wrapContentWidth()
        params.width.toIntOrNull() != null -> result.width(params.width.toInt().dp)
        params.width.isEmpty() -> result
        else -> throw IllegalArgumentException()
    }
    result = when {
        params.height == "fillMax" -> result.fillMaxHeight()
        params.height == "wrapContent" -> result.wrapContentHeight()
        params.height.toIntOrNull() != null -> result.height(params.height.toInt().dp)
        params.height.isEmpty() -> result
        else -> throw IllegalArgumentException()
    }
    if (params.padding != null) {
        result = result.padding(
            params.padding.run {
                PaddingValues(
                    start = start.dp,
                    end = end.dp,
                    top = top.dp,
                    bottom = bottom.dp,
                )
            }
        )
    }
    return result
}