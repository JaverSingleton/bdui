package ru.javersingleton.bdui.v3.core.interaction

import ru.javersingleton.bdui.v3.core.field.FieldsHolder
import ru.javersingleton.bdui.v3.core.field.StructureField

interface InteractionProcessor {

    fun process(interaction: Interaction)

}

interface Interaction {

    val type: String

    val name: String

    val params: StructureField?

}

fun FieldsHolder.getInteraction(refName: String): Interaction? =
    getField(refName) as Interaction?

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