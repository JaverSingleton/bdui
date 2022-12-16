package ru.javersingleton.bdui.render.android_view

import ru.javersingleton.bdui.engine.ComponentStateFactory
import ru.javersingleton.bdui.engine.register.ByTypeStrategy

abstract class ComponentFactory<T>(
    val stateFactory: ComponentStateFactory<T>
) : ByTypeStrategy.Element<String> {

    abstract fun createComponent(context: BeduinViewContext): Component

}