package ru.javersingleton.bdui.component.lazy_column.compose

import androidx.compose.runtime.Immutable
import ru.javersingleton.bdui.component.common.Padding
import ru.javersingleton.bdui.engine.ComponentStateFactory
import ru.javersingleton.bdui.engine.field.entity.ComponentData

@Immutable
data class ImmutableList(
    val value: List<LazyColumnState.Child>,
)

data class LazyColumnState(
    val children: ImmutableList,
) {

    @Immutable
    data class Child(
        val component: ComponentData,
        val params: Params
    ) {
        @Immutable
        data class Params(
            val width: String,
            val height: String,
            val padding: Padding?,
        )
    }

}

object LazyColumnStateFactory : ComponentStateFactory<LazyColumnState>() {

    override val type: String = "LazyColumn"

    override fun Scope.create(componentType: String): LazyColumnState = LazyColumnState(
        children = ImmutableList(
            prop("children").asList {
                asComponentWithParams { component ->
                    LazyColumnState.Child(
                        component,
                        LazyColumnState.Child.Params(
                            width = prop("layout_width").asString() ?: "fillMax",
                            height = prop("layout_height").asString() ?: "wrapContent",
                            padding = prop("layout_padding").asObject {
                                Padding.create(this@create, this)
                            },
                        )
                    )
                }
            }.filterNotNull()
        )
    )

}
