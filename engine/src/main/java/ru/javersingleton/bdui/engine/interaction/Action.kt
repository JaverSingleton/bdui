package ru.javersingleton.bdui.engine.interaction

import ru.javersingleton.bdui.engine.core.Value
import ru.javersingleton.bdui.engine.field.ComponentField

interface Action : Interaction {
    fun run(state: ComponentField): ActionResult

    interface Factory : Interaction.Factory {
        override fun create(params: Map<String, Value<*>>): Action
    }

    companion object {
        val TYPE = "action"
    }

}

data class PlainAction(
    val interaction: Interaction
) : Action {

    override fun run(state: ComponentField): ActionResult = interaction.asActionResult()

}