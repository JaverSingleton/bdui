package ru.javersingleton.bdui.engine.interaction

import ru.javersingleton.bdui.engine.register.Register
import ru.javersingleton.bdui.engine.core.Value

interface Interaction {

    fun asAction(): Action = when (this) {
        is Action -> this
        is ActionResult -> PlainAction(this)
        else -> throw IllegalArgumentException("$this is not an Action")
    }

    fun asActionResult(): ActionResult = when (this) {
        is ActionResult -> this
        else -> throw IllegalArgumentException("$this is not an ActionResult")
    }

    interface Factory : Register.Element {

        fun create(
            params: Map<String, Value<*>>
        ): Interaction

    }

}