package ru.javersingleton.bdui.core.interaction

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import ru.javersingleton.bdui.core.Value
import ru.javersingleton.bdui.core.field.ComponentField

interface Action : Interaction {
    fun run(state: ComponentField): Flow<ActionResult>

    interface Factory : Interaction.Factory {
        override fun create(params: Map<String, Value<*>>): Action
    }
}

data class PlainAction(
    val interaction: Interaction
) : Action {

    override fun run(state: ComponentField): Flow<ActionResult> = flowOf(interaction.asActionResult())

}