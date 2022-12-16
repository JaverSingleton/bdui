package ru.javersingleton.bdui.component.row.compose

import ru.javersingleton.bdui.component.common.ImmutableList
import ru.javersingleton.bdui.component.common.Padding
import ru.javersingleton.bdui.engine.ComponentStateFactory
import ru.javersingleton.bdui.engine.field.entity.ComponentData

object RowStateFactory : ComponentStateFactory<RowState>() {

    override val type: String = "Row"

    override fun Scope.create(componentType: String): RowState = RowState(
        children = prop("children").asList {
            asComponentWithParams { component ->
                RowState.Child(
                    component,
                    RowState.Child.Params(
                        width = prop("layout_width").asString() ?: "fillMax",
                        height = prop("layout_height").asString() ?: "wrapContent",
                        padding = prop("layout_padding").asObject {
                            Padding.create(this@create, this)
                        },
                    )
                )
            }
        }.filterNotNull().let { ImmutableList(it) }
    )

}

data class RowState(
    val children: ImmutableList<Child>,
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