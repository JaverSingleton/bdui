package ru.javersingleton.bdui.component.text.compose

import ru.javersingleton.bdui.engine.ComponentStateFactory

object TextStateFactory : ComponentStateFactory<TextState>() {

    override val key: String = "Text"

    override fun Scope.create(componentType: String): TextState = TextState(
        text = prop("text").asString() ?: "",
        textSize = prop("textSize").asInt() ?: 16,
        textAlign = prop("textAlign").asString() ?: "Start"
    )

}

data class TextState(
    val text: String,
    val textAlign: String,
    val textSize: Int
)