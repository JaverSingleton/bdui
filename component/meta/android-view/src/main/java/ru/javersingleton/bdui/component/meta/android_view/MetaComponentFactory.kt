package ru.javersingleton.bdui.component.meta.android_view

import ru.javersingleton.bdui.component.meta.state.MetaState
import ru.javersingleton.bdui.component.meta.state.MetaStateFactory
import ru.javersingleton.bdui.render.android_view.BeduinViewContext
import ru.javersingleton.bdui.render.android_view.Component
import ru.javersingleton.bdui.render.android_view.ComponentFactory

object MetaComponentFactory : ComponentFactory<MetaState>(MetaStateFactory) {

    override val type: String = "Meta"

    override fun createComponent(context: BeduinViewContext): Component = MetaComponent(context)

}