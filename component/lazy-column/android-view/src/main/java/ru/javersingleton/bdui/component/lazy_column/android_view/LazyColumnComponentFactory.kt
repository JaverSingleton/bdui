package ru.javersingleton.bdui.component.lazy_column.android_view

import ru.javersingleton.bdui.component.lazy_column.state.LazyColumnState
import ru.javersingleton.bdui.component.lazy_column.state.LazyColumnStateFactory
import ru.javersingleton.bdui.render.android_view.BeduinViewContext
import ru.javersingleton.bdui.render.android_view.Component
import ru.javersingleton.bdui.render.android_view.ComponentFactory

object LazyColumnComponentFactory: ComponentFactory<LazyColumnState>(LazyColumnStateFactory) {

    override val type: String = "LazyColumn"

    override fun createComponent(context: BeduinViewContext): Component =
        LazyColumnComponent(context)

}