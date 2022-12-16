package ru.javersingleton.bdui.render.android_view

import ru.javersingleton.bdui.engine.register.ByTypeStrategy
import ru.javersingleton.bdui.engine.register.CommonRegister
import ru.javersingleton.bdui.engine.register.ComponentStatesRegister
import ru.javersingleton.bdui.engine.register.MutableRegister

class ComponentsRegister(
    private val delegate: CommonRegister<ComponentFactory<*>, String> = CommonRegister(
        registerType = "Component",
        accessStrategy = ByTypeStrategy()
    )
) : MutableRegister<ComponentFactory<*>, String> by delegate {

    fun createComponentStatesRegister(): ComponentStatesRegister =
        ComponentStatesRegister().apply {
            register(
                *delegate.elements.map { it.stateFactory }.toTypedArray(),
            ) { type: String -> delegate.handleDefault(type).stateFactory }
        }

}