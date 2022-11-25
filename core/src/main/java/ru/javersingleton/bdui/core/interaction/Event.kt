package ru.javersingleton.bdui.core.interaction

import ru.javersingleton.bdui.core.Value

interface Event : ActionResult {

    interface Factory : Interaction.Factory {
        override fun create(params: Map<String, Value<*>>): Event
    }

}