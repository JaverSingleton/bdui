package ru.javersingleton.bdui.handler.flow

import ru.javersingleton.bdui.engine.field.ComponentField
import ru.javersingleton.bdui.engine.interaction.Interaction

interface EffectHandler : InteractionHandler {

    fun handle(
        currentState: ComponentField,
        interaction: Interaction
    ): ComponentField

}

abstract class BaseEffectHandler<T : Interaction>(
    override val stateFactory: Interaction.Factory
) : EffectHandler {

    @Suppress("UNCHECKED_CAST")
    override fun handle(
        currentState: ComponentField,
        interaction: Interaction
    ): ComponentField = handleEffect(
        currentState,
        interaction as T
    )

    abstract fun handleEffect(
        currentState: ComponentField,
        effect: T
    ): ComponentField

}