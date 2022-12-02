package ru.javersingleton.bdui.interaction.base.effect

import ru.javersingleton.bdui.engine.register.EffectsRegister

fun EffectsRegister.withBase(): EffectsRegister = apply {
    register(
        StatePatchEffect.Factory
    )
}