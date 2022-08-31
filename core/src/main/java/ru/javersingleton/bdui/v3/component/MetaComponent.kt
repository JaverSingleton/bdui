package ru.javersingleton.bdui.v3.component

import android.content.Context
import android.view.View
import ru.javersingleton.bdui.v3.*

class MetaComponent(
    defaultState: RawState,
    private val rootTemplate: ComponentTemplate
) : ViewComponent(defaultState) {

    private lateinit var rootComponent: ViewComponent

    override fun onCreateView(context: Context, emptyState: State): View {
        rootComponent = inflateChild(rootTemplate.resolve(emptyState))
        return rootComponent.view
    }

    override fun onBindView(state: State) {
        rootComponent.render(rootTemplate.resolve(state))
    }

}


data class MetaComponentData(
    val defaultFields: RawState,
    val root: ComponentTemplate
)