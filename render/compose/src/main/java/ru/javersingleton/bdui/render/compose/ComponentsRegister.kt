package ru.javersingleton.bdui.render.compose

import ru.javersingleton.bdui.engine.register.ComponentStatesRegister
import ru.javersingleton.bdui.engine.register.MutableRegister
import ru.javersingleton.bdui.engine.register.StaticKeyRegister

class ComponentsRegister(
    private val delegate: StaticKeyRegister<ComponentRender<*>, String> = StaticKeyRegister("Component")
) : MutableRegister<ComponentRender<*>, String> by delegate {

    fun createComponentStatesRegister(): ComponentStatesRegister =
        ComponentStatesRegister().apply {
            register(
                *delegate.elements.map { it.stateFactory }.toTypedArray(),
            ) { type: String -> delegate.handleDefault(type).stateFactory }
        }

}