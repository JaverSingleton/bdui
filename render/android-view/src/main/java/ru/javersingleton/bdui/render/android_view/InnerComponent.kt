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
    private val view get() = component.view

    fun createOrUpdateView(
        parent: ViewGroup,
        state: ComponentData
    ): View {
        val targetComponentFactory = context.inflateComponentFactory(state.componentType)
        if (componentFactory == targetComponentFactory) {
            component.bindState(state.value)
            return view
        }

        componentFactory = targetComponentFactory
        _component = targetComponentFactory.createComponent().apply {
            createView(parent)
            bindState(state.value)
        }

        return view
    }

}