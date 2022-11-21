package ru.javersingleton.bdui.component.state

import ru.javersingleton.bdui.core.ComponentState
import ru.javersingleton.bdui.core.field.ComponentStructure

object RowComponent {

    object StateFactory : ComponentState.Factory<RowState>() {

        override fun Scope.create(componentType: String): RowState = RowState(
            children = prop("children").asList {
                asComponentWithParams { component ->
                    RowState.Child(
                        component,
                        RowState.Child.Params(
                            width = prop("layout_width").asString() ?: "fillMaxWidth",
                            height = prop("layout_height").asString() ?: "wrapContentHeight",
                        )
                    )
                }
            }.filterNotNull()
        )

    }

}

data class RowState(
    val children: List<Child>,
) {

    data class Child(
        val component: ComponentStructure,
        val params: Params
    ) {
        data class Params(
            val width: String,
            val height: String,
        )
    }
}