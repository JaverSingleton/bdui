package ru.javersingleton.bdui.engine.register

import ru.javersingleton.bdui.engine.ComponentStateFactory

class ComponentStatesRegister :
    MutableRegister<ComponentStateFactory<*>, String> by CommonRegister(
        registerType = "ComponentState",
        accessStrategy = ByTypeStrategy()
    )