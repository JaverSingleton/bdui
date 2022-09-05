package ru.javersingleton.bdui.v3.core.interaction

import ru.javersingleton.bdui.v3.RawState
import ru.javersingleton.bdui.v3.RawStatePatch

interface Effect {

    interface Factory {

        fun create(factory: InteractionFactory, interaction: Interaction): Effect

    }

    interface Processor<T : Effect> {

        fun process(state: RawState, effect: T): RawState = state

        fun convertToEvent(state: RawState, effect: T): Event? = null

    }

}

data class BlankEffect(
    val event: Event
) : Effect {

    object Factory : Effect.Factory {

        override fun create(factory: InteractionFactory, interaction: Interaction): Effect =
            BlankEffect(event = factory.createEvent(interaction))

    }

    object Processor : Effect.Processor<BlankEffect> {

        override fun convertToEvent(state: RawState, effect: BlankEffect): Event = effect.event

    }

}

data class PatchEffect(
    val statePatch: RawStatePatch
): Effect {

    object Factory : Effect.Factory {

        override fun create(factory: InteractionFactory, interaction: Interaction): Effect =
            PatchEffect(statePatch = RawStatePatch(interaction.params))

    }

    object Processor : Effect.Processor<BlankEffect> {

        override fun convertToEvent(state: RawState, effect: BlankEffect): Event = effect.event

    }

}