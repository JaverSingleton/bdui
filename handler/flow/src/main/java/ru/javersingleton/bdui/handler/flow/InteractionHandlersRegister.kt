package ru.javersingleton.bdui.handler.flow

import ru.javersingleton.bdui.engine.interaction.Interaction
import ru.javersingleton.bdui.engine.register.ByRelevantStrategy
import ru.javersingleton.bdui.engine.register.CommonRegister
import ru.javersingleton.bdui.engine.register.InteractionsRegister
import ru.javersingleton.bdui.engine.register.MutableRegister

class InteractionHandlersRegister(
    private val delegate: CommonRegister<InteractionHandler, Interaction> = CommonRegister(
        registerType = "InteractionHandler",
        accessStrategy = ByRelevantStrategy()
    )
) : MutableRegister<InteractionHandler, Interaction> by delegate {

    fun createInteractionsRegister(): InteractionsRegister =
        InteractionsRegister().apply {
            register(
                *delegate.elements.map { it.stateFactory }.toTypedArray(),
            )
        }

}