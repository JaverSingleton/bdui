package ru.javersingleton.bdui.component.meta.android_view

import android.view.View
import android.view.ViewGroup
import ru.javersingleton.bdui.component.meta.state.MetaState
import ru.javersingleton.bdui.engine.core.Value
import ru.javersingleton.bdui.engine.core.currentQuiet
import ru.javersingleton.bdui.render.android_view.BeduinViewContext
import ru.javersingleton.bdui.render.android_view.Component
import ru.javersingleton.bdui.render.android_view.InnerComponent

class MetaComponent(
    private val beduinViewContext: BeduinViewContext
) : Component {

    private val rootComponent: InnerComponent by lazy { InnerComponent(beduinViewContext) }

    override fun createOrUpdateView(parent: ViewGroup, state: Value<*>): View {
        @Suppress("UNCHECKED_CAST")
        val componentState = (state as Value<MetaState>).currentQuiet
        return rootComponent.createOrUpdateView(parent, componentState!!.childComponent)
    }

    override fun updateState(state: Value<*>) {
        @Suppress("UNCHECKED_CAST")
        val componentState = (state as Value<MetaState>).currentQuiet
        rootComponent.updateState(componentState!!.childComponent)
    }

}