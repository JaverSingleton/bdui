package ru.javersingleton.bdui.interaction.base.effect

import ru.javersingleton.bdui.engine.register.InteractionsRegister

fun InteractionsRegister.withBase(): InteractionsRegister = apply {
    register(
        StatePatchEffect.Factory
    )
}