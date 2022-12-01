package ru.javersingleton.bdui.component.state

import ru.javersingleton.bdui.component.state.ColumnComponent.StateFactory.create
import ru.javersingleton.bdui.component.state.entity.Padding
import ru.javersingleton.bdui.core.ComponentState
import ru.javersingleton.bdui.core.field.ComponentData

object LazyColumnComponent {

    object StateFactory : ComponentState.Factory<LazyColumnState>() {

        override fun Scope.create(componentType: String): LazyColumnState = LazyColumnState(
            children = prop("children").asList {
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

    }

}

data class LazyColumnState(
    val children: List<Child>,
) {

    data class Child(
        val component: ComponentData,
        val params: Params
    ) {
        data class Params(
            val width: String,
            val height: String,
            val padding: Padding?,
        )
    }

}
