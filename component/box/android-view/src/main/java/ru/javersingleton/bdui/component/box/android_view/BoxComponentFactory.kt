package ru.javersingleton.bdui.component.box.android_view

import ru.javersingleton.bdui.component.box.state.BoxState
import ru.javersingleton.bdui.component.box.state.BoxStateFactory
import ru.javersingleton.bdui.render.android_view.BeduinViewContext
import ru.javersingleton.bdui.render.android_view.Component
import ru.javersingleton.bdui.render.android_view.ComponentFactory

object BoxComponentFactory : ComponentFactory<BoxState>(BoxStateFactory) {

    override val type: String
        get() = "Box"

    override fun createComponent(context: BeduinViewContext): Component = BoxComponent(context)

}