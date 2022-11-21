package ru.javersingleton.bdui.component.state

import ru.javersingleton.bdui.core.ComponentState
import ru.javersingleton.bdui.core.field.ComponentStructure

object BoxComponent {

    object StateFactory : ComponentState.Factory<BoxState>() {

        override fun Scope.create(componentType: String): BoxState =
            BoxState(
                children = prop("children").asList {
                    asComponentWithParams { component ->
                        BoxState.Child(
                            component,
                            BoxState.Child.Params(
                                alignment = prop("layout_alignment").asString() ?: "TopCenter",
                                width = prop("layout_width").asString() ?: "fillMaxWidth",
                                height = prop("layout_height").asString() ?: "wrapContentHeight",
                            )
                        )
                    }
                }.filterNotNull(),
                backgroundColor = prop("backgroundColor").asInt() ?: 0
            )

    }

}

data class BoxState(
    val children: List<Child>,
    val backgroundColor: Int,
) {

    data class Child(
        val component: ComponentStructure,
        val params: Params
    ) {
        data class Params(
            val alignment: String,
            val width: String,
            val height: String,
        )
    }
}