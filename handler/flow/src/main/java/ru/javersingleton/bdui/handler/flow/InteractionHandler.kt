package ru.javersingleton.bdui.handler.flow

import ru.javersingleton.bdui.engine.interaction.Interaction
import ru.javersingleton.bdui.engine.register.DynamicKeyRegister

interface InteractionHandler : DynamicKeyRegister.Element<Interaction> {
    val stateFactory: Interaction.Factory
}