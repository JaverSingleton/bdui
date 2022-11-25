package ru.javersingleton.bdui.core.interaction

import ru.javersingleton.bdui.core.Value

interface Interaction {

    fun asAction(): Action = when(this) {
        is Action -> this
        is ActionResult -> PlainAction(this)
        else -> throw IllegalArgumentException("$this is not an Action")
    }

    fun asActionResult(): ActionResult = when(this) {
        is ActionResult -> this
        else -> throw IllegalArgumentException("$this is not an ActionResult")
    }

    interface Factory {

        fun create(
            params: Map<String, Value<*>>
        ): Interaction

    }

}