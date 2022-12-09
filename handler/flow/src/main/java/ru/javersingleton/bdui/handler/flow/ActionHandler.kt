package ru.javersingleton.bdui.handler.flow

import kotlinx.coroutines.flow.Flow
import ru.javersingleton.bdui.engine.field.ComponentField
import ru.javersingleton.bdui.engine.interaction.Interaction

interface ActionHandler : InteractionHandler {

    fun handle(
        currentState: ComponentField,
        interaction: Interaction
    ): Flow<Interaction>

}

abstract class BaseActionHandler<T : Interaction>(
    override val stateFactory: Interaction.Factory
) : ActionHandler {

    @Suppress("UNCHECKED_CAST")
    override fun handle(
        currentState: ComponentField,
        interaction: Interaction
    ): Flow<Interaction> = handleAction(
        currentState,
        interaction as T
    )

    abstract fun handleAction(
        currentState: ComponentField,
        action: T
    ): Flow<Interaction>

}

