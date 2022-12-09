package ru.javersingleton.bdui.handler.flow

import ru.javersingleton.bdui.engine.field.ComponentField
import ru.javersingleton.bdui.engine.interaction.Interaction

interface EventHandler : InteractionHandler {

    fun handle(
        currentState: ComponentField,
        interaction: Interaction
    )

}

abstract class BaseEventHandler<T : Interaction>(
    override val stateFactory: Interaction.Factory
) : EventHandler {

    @Suppress("UNCHECKED_CAST")
    override fun handle(
        currentState: ComponentField,
        interaction: Interaction
    ) = handleEvent(
        currentState,
        interaction as T
    )

    abstract fun handleEvent(
        currentState: ComponentField,
        event: T
    )

}