package ru.javersingleton.bdui.core.interaction

import ru.javersingleton.bdui.core.Value
import ru.javersingleton.bdui.core.field.ComponentField

interface Effect : ActionResult {

    fun run(state: ComponentField): ComponentField

    interface Factory : Interaction.Factory {
        override fun create(params: Map<String, Value<*>>): Effect
    }

}