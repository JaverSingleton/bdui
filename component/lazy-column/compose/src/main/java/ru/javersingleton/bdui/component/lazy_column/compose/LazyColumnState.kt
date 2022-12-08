package ru.javersingleton.bdui.component.lazy_column.compose

import ru.javersingleton.bdui.component.common.Padding
import ru.javersingleton.bdui.engine.ComponentStateFactory
import ru.javersingleton.bdui.engine.field.ComponentData

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

object LazyColumnStateFactory : ComponentStateFactory<LazyColumnState>() {

    override val type: String = "LazyColumn"

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
