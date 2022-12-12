package ru.javersingleton.bdui.component.column.compose

import ru.javersingleton.bdui.component.common.Padding
import ru.javersingleton.bdui.engine.ComponentStateFactory
import ru.javersingleton.bdui.engine.field.entity.ComponentData

data class ColumnState(
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

object ColumnStateFactory : ComponentStateFactory<ColumnState>() {

    override val type: String = "Column"

    override fun Scope.create(componentType: String): ColumnState = ColumnState(
        children = prop("children").asList {
            asComponentWithParams { component ->
                ColumnState.Child(
                    component,
                    ColumnState.Child.Params(
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