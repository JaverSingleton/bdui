package ru.javersingleton.bdui.engine.interaction

import ru.javersingleton.bdui.engine.core.Value

interface Event : ActionResult {

    interface Factory : Interaction.Factory {
        override fun create(params: Map<String, Value<*>>): Event
    }

    companion object {
        val TYPE = "event"
    }

}