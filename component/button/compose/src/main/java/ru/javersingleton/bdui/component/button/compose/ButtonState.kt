package ru.javersingleton.bdui.component.button.compose

import ru.javersingleton.bdui.engine.ComponentStateFactory

data class ButtonState(
    val text: String,
    val onClick: (() -> Unit)?,
)

object ButtonStateFactory : ComponentStateFactory<ButtonState>() {

    override val key: String = "Button"

    override fun Scope.create(componentType: String): ButtonState = ButtonState(
        text = prop("text").asString() ?: "",
        onClick = prop("onClick").asInteraction()?.let { callback ->
            {
                callback(mapOf())
            }
        }
    )

}