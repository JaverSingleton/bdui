package ru.javersingleton.bdui.v3.core.interaction

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface Action {

    interface Factory {

        fun create(factory: InteractionFactory, interaction: Interaction): Action

    }

    interface Processor<T : Action> {

        fun process(action: T): Flow<Effect>

    }

}

data class BlankAction(
    val effect: Effect
) : Action {

    object Processor : Action.Processor<BlankAction> {
        override fun process(action: BlankAction): Flow<Effect> = flowOf(action.effect)
    }

    object Factory : Action.Factory {

        override fun create(factory: InteractionFactory, interaction: Interaction): Action =
            BlankAction(
                effect = factory.createEffect(interaction)
            )

    }

}

