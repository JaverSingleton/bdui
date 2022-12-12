package ru.javersingleton.bdui.render.android_view

import android.view.View
import android.view.ViewGroup
import ru.javersingleton.bdui.engine.field.entity.ComponentData

class InnerComponent(
    private val context: BeduinViewContext
) {

    private var componentFactory: ComponentFactory<*>? = null
    private var _component: Component? = null
    private val component: Component get() = _component!!

    fun createOrUpdateView(
        parent: ViewGroup,
        state: ComponentData
    ): View {
        val targetComponentFactory = context.inflateComponentFactory(state.componentType)
        if (componentFactory == targetComponentFactory) {
            return component.createOrUpdateView(parent, state.value)
        }

        componentFactory = targetComponentFactory
        _component = targetComponentFactory.createComponent(context)

        return component.createOrUpdateView(parent, state.value)
    }

}