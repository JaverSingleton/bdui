package ru.javersingleton.bdui.render.compose

import ru.javersingleton.bdui.engine.register.ByTypeStrategy
import ru.javersingleton.bdui.engine.register.CommonRegister
import ru.javersingleton.bdui.engine.register.ComponentStatesRegister
import ru.javersingleton.bdui.engine.register.MutableRegister

class ComponentsRegister(
    private val delegate: CommonRegister<ComponentRender<*>, String> = CommonRegister(
        registerType = "Component",
        accessStrategy = ByTypeStrategy()
    )
) : MutableRegister<ComponentRender<*>, String> by delegate {

    fun createComponentStatesRegister(): ComponentStatesRegister =
        ComponentStatesRegister().apply {
            register(
                *delegate.elements.map { it.stateFactory }.toTypedArray(),
            ) { type: String -> delegate.handleDefault(type).stateFactory }
        }

}