package ru.javersingleton.bdui.component.state

import ru.javersingleton.bdui.core.ComponentState
import ru.javersingleton.bdui.core.field.ComponentStructure

object BoxComponent {

    class StateFactory : ComponentState.Factory<BoxState>() {

        override fun Scope.create(componentType: String): BoxState =
            BoxState(
                children = prop("children").toArray {
                    toComponentWithParams { component ->
                        Child(
                            component,
                            Child.Params(
                                alignment = prop("alignment").toStringValue(),
                            )
                        )
                    }
                },
                backgroundColor = prop("alignment").toInt()
            )

    }

    data class BoxState(
        val children: List<Child>,
        val backgroundColor: Int,
    )

    data class Child(
        val component: ComponentStructure,
        val params: Params
    ) {
        data class Params(
            val alignment: String,
        )
    }


}