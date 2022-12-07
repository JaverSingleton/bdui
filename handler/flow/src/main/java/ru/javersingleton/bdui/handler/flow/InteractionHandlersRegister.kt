package ru.javersingleton.bdui.handler.flow

import ru.javersingleton.bdui.engine.interaction.Interaction
import ru.javersingleton.bdui.engine.register.DynamicKeyRegister
import ru.javersingleton.bdui.engine.register.InteractionsRegister
import ru.javersingleton.bdui.engine.register.MutableRegister

class InteractionHandlersRegister(
    private val delegate: DynamicKeyRegister<InteractionHandler, Interaction> = DynamicKeyRegister("InteractionHandler")
) : MutableRegister<InteractionHandler, Interaction> by delegate {


    fun createInteractionsRegister(): InteractionsRegister =
        InteractionsRegister().apply {
            register(
                *delegate.elements.map { it.stateFactory }.toTypedArray(),
            )
        }

}