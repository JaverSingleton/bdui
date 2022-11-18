package ru.javersingleton.bdui.component.state

import ru.javersingleton.bdui.core.ComponentState
import ru.javersingleton.bdui.core.field.ComponentStructure

object TextComponent {

    object StateFactory : ComponentState.Factory<TextState>() {

        override fun Scope.create(componentType: String): TextState = TextState(
            text = prop("text").toStringValue() ?: "",
            textSize = prop("textSize").toInt() ?: 16
        )

    }

}

data class TextState(
    val text: String,
    val textSize: Int
)