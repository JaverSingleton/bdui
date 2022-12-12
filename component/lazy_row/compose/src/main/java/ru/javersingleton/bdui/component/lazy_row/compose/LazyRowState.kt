package ru.javersingleton.bdui.component.lazy_row.compose

import ru.javersingleton.bdui.component.common.Padding
import ru.javersingleton.bdui.engine.ComponentStateFactory
import ru.javersingleton.bdui.engine.field.entity.ComponentData

data class LazyRowState(
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

object LazyRowStateFactory : ComponentStateFactory<LazyRowState>() {

    override val type: String = "LazyRow"

    override fun Scope.create(componentType: String): LazyRowState = LazyRowState(
        children = prop("children").asList {
            asComponentWithParams { component ->
                LazyRowState.Child(
                    component,
                    LazyRowState.Child.Params(
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
