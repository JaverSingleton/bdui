package ru.javersingleton.bdui.v3.core.interaction

import ru.javersingleton.bdui.v3.core.field.FieldsHolder
import ru.javersingleton.bdui.v3.core.field.StructureField

interface InteractionProcessor {

    fun process(interaction: Interaction)

}

data class Interaction(
    val type: String,
    val name: String,
    val params: StructureField?,
) {

    constructor(fieldsHolder: FieldsHolder): this(
        type = fieldsHolder.extractType(),
        name = fieldsHolder.extractName(),
        params = fieldsHolder.getStructureField("params")
    )

    companion object {

        private fun FieldsHolder.extractType(): String = when {
            hasField("actionName") -> "action"
            hasField("effectName") -> "effect"
            hasField("eventName") -> "event"
            else -> throw IllegalArgumentException()
        }

        private fun FieldsHolder.extractName() =
            getString("${extractType()}Name")!!
    }

}

fun FieldsHolder.getInteraction(refName: String): Interaction? =
    getStructureField(refName)?.let { Interaction(it) }

class CompositeInteractionFactory(
    private val actionFactories: Map<String, Action.Factory>,
    private val effectFactories: Map<String, Effect.Factory>,
    private val eventFactories: Map<String, Event.Factory>,
): InteractionFactory {

    override fun createAction(interaction: Interaction): Action {
        if (interaction.type != "action") {
            return BlankAction.Factory.create(this, interaction)
        }

        return actionFactories[interaction.name]!!.create(this, interaction)
    }

    override fun createEffect(interaction: Interaction): Effect {
        if (interaction.type != "effect") {
            return BlankEffect.Factory.create(this, interaction)
        }

        return effectFactories[interaction.name]!!.create(this, interaction)
    }

    override fun createEvent(interaction: Interaction): Event {
        return eventFactories[interaction.name]!!.create(this, interaction)
    }

}

interface InteractionFactory {
    fun createAction(interaction: Interaction): Action
    fun createEffect(interaction: Interaction): Effect
    fun createEvent(interaction: Interaction): Event
}