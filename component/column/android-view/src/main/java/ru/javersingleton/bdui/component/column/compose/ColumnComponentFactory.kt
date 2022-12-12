package ru.javersingleton.bdui.component.column.compose

import ru.javersingleton.bdui.component.column.state.ColumnState
import ru.javersingleton.bdui.component.column.state.ColumnStateFactory
import ru.javersingleton.bdui.render.android_view.BeduinViewContext
import ru.javersingleton.bdui.render.android_view.Component
import ru.javersingleton.bdui.render.android_view.ComponentFactory

object ColumnComponentFactory: ComponentFactory<ColumnState>(ColumnStateFactory) {

    override val type: String
        get() = "Column"

    override fun createComponent(context: BeduinViewContext): Component =
        ColumnComponent(context)
}