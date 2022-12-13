package ru.javersingleton.bdui.component.box.state

import ru.javersingleton.bdui.component.common.Padding
import ru.javersingleton.bdui.component.common.Padding.Companion.create
import ru.javersingleton.bdui.engine.ComponentStateFactory
import ru.javersingleton.bdui.engine.field.entity.ComponentData

data class BoxState(
    val children: List<Child>,
    val backgroundColor: String,
    val onClick: (() -> Unit)?,
) {

    data class Child(
        val component: ComponentData,
        val params: Params,
    ) {
        data class Params(
            val alignment: String,
            val width: String,
            val height: String,
            val padding: Padding?,
        )
    }
}

object BoxStateFactory : ComponentStateFactory<BoxState>() {

    override val type: String = "Box"

    override fun Scope.create(componentType: String): BoxState =
        BoxState(
            children = prop("children").asList {
                asComponentWithParams { component ->
                    BoxState.Child(
                        component,
                        BoxState.Child.Params(
                            alignment = prop("layout_alignment").asString() ?: "TopCenter",
                            width = prop("layout_width").asString() ?: "fillMax",
                            height = prop("layout_height").asString() ?: "wrapContent",
                            padding = prop("layout_padding").asObject {
                                create(this@create, this)
                            }
                        )
                    )
                }
            }.filterNotNull(),
            backgroundColor = prop("backgroundColor").asString() ?: "#00000000",
            onClick = prop("onClick").asInteraction()?.let { callback ->
                {
                    callback.invoke(mapOf())
                }
            }
        )

}
