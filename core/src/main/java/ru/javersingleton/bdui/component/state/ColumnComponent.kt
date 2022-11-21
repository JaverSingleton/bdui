package ru.javersingleton.bdui.component.state

import ru.javersingleton.bdui.core.ComponentState
import ru.javersingleton.bdui.core.field.ComponentStructure

object ColumnComponent {

    object StateFactory : ComponentState.Factory<ColumnState>() {

        override fun Scope.create(componentType: String): ColumnState = ColumnState(
            children = prop("children").asList {
                asComponentWithParams { component ->
                    ColumnState.Child(
                        component,
                        ColumnState.Child.Params(
                            width = prop("layout_width").asString() ?: "fillMaxWidth",
                            height = prop("layout_height").asString() ?: "wrapContentHeight",
                        )
                    )
                }
            }.filterNotNull()
        )

    }

}

data class ColumnState(
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