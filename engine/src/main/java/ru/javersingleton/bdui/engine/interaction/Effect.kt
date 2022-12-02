package ru.javersingleton.bdui.engine.interaction

import ru.javersingleton.bdui.engine.core.Value
import ru.javersingleton.bdui.engine.field.ComponentField

interface Effect : ActionResult {

    fun run(state: ComponentField): ComponentField

    interface Factory : Interaction.Factory {
        override fun create(params: Map<String, Value<*>>): Effect
    }

    companion object {
        val TYPE = "effect"
    }

}