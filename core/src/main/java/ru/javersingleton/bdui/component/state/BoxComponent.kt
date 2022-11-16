package ru.javersingleton.bdui.component.state

import ru.javersingleton.bdui.core.ComponentState
import ru.javersingleton.bdui.core.field.ComponentStructure

object BoxComponent {

    class StateFactory : ComponentState.Factory<BoxState>() {

        override fun Scope.create(componentType: String): BoxState =
            BoxState(
                child = toComponentWithParams("child") { component ->
                    Child(
                        component,
                        Child.Params(
                            alignment = toString("alignment"),
                        )
                    )
                },
                backgroundColor = toInt("backgroundColor")
            )

    }

    data class BoxState(
        val child: Child,
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