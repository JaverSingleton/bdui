package ru.javersingleton.bdui.handler.flow

import ru.javersingleton.bdui.engine.interaction.Interaction
import ru.javersingleton.bdui.engine.register.ByRelevantStrategy

interface InteractionHandler : ByRelevantStrategy.Element<Interaction> {
    val stateFactory: Interaction.Factory
}