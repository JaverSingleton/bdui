package ru.javersingleton.bdui.render.compose

import ru.javersingleton.bdui.engine.register.CommonRegister
import ru.javersingleton.bdui.engine.register.ComponentStatesRegister
import ru.javersingleton.bdui.engine.register.MutableRegister

class ComponentsRegister(
    private val delegate: CommonRegister<ComponentRender<*>> = CommonRegister("Component")
) :
    MutableRegister<ComponentRender<*>> by delegate {

    fun createComponentStatesRegister(): ComponentStatesRegister =
        ComponentStatesRegister().apply {
            register(
                *delegate.elements.map { it.stateFactory }.toTypedArray(),
            ) { type: String -> delegate.handleDefault(type).stateFactory }
        }

}