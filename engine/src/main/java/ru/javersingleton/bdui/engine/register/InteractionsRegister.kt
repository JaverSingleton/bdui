package ru.javersingleton.bdui.engine.register

import ru.javersingleton.bdui.engine.interaction.Interaction

class InteractionsRegister : MutableRegister<Interaction.Factory, String> by CommonRegister(
    registerType = "Interaction",
    accessStrategy = ByTypeStrategy()
)