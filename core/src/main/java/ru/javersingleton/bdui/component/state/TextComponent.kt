package ru.javersingleton.bdui.component.state

import ru.javersingleton.bdui.core.ComponentState

object TextComponent {

    object StateFactory : ComponentState.Factory<TextState>() {

        override fun Scope.create(componentType: String): TextState = TextState(
            text = prop("text").asString() ?: "",
            textSize = prop("textSize").asInt() ?: 16
        )

    }

}

data class TextState(
    val text: String,
    val textSize: Int
)