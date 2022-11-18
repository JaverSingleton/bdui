package ru.javersingleton.bdui.component.state

import ru.javersingleton.bdui.core.ComponentState
import ru.javersingleton.bdui.core.field.ComponentStructure

object BoxComponent {

    object StateFactory : ComponentState.Factory<BoxState>() {

        override fun Scope.create(componentType: String): BoxState =
            BoxState(
                children = prop("children").toArray {
                    toComponentWithParams { component ->
                        BoxState.Child(
                            component,
                            BoxState.Child.Params(
                                alignment = prop("layout_alignment").toStringValue() ?: "TopCenter",
                            )
                        )
                    }
                }.filterNotNull(),
                backgroundColor = prop("backgroundColor").toInt() ?: 0
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
        )
    }
}