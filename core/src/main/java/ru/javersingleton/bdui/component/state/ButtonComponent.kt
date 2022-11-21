package ru.javersingleton.bdui.component.state

import ru.javersingleton.bdui.core.ComponentState

object ButtonComponent {

    object StateFactory : ComponentState.Factory<ButtonState>() {

        override fun Scope.create(componentType: String): ButtonState = ButtonState(
            text = prop("text").asString() ?: "",
        )

    }

}

data class ButtonState(
    val text: String,
)