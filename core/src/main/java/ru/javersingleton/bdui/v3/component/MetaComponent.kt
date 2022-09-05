package ru.javersingleton.bdui.v3.component

import android.content.Context
import android.view.View
import ru.javersingleton.bdui.v3.*
import ru.javersingleton.bdui.v3.core.component.RawTemplate

class MetaComponent(
    defaultState: RawState,
    private val rootTemplate: RawTemplate
) : ViewComponent(defaultState) {

    private lateinit var rootComponent: ViewComponent

    override fun onCreateView(context: Context, emptyState: State): View {
        rootComponent = inflateChild(rootTemplate.resolve(emptyState))
        return rootComponent.view
    }

    override fun onRenderState(state: State) {
        rootComponent.render(rootTemplate.resolve(state).statePatch)
    }

}


data class MetaComponentData(
    val defaultFields: RawState,
    val rootComponent: RawTemplate
)