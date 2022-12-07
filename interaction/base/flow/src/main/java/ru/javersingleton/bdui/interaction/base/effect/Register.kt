package ru.javersingleton.bdui.interaction.base.effect

import ru.javersingleton.bdui.handler.flow.InteractionHandlersRegister
import ru.javersingleton.bdui.interaction.delay.flow.DelayActionHandler
import ru.javersingleton.bdui.interaction.state_patch.flow.StatePatchEffectHandler

fun InteractionHandlersRegister.withBase(): InteractionHandlersRegister = apply {
    register(
        StatePatchEffectHandler,
        DelayActionHandler
    )
}