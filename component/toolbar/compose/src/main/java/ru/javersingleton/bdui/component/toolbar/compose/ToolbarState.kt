package ru.javersingleton.bdui.component.toolbar.compose

import ru.javersingleton.bdui.engine.ComponentStateFactory

object ToolbarStateFactory : ComponentStateFactory<ToolbarState>() {

    override val key: String = "Toolbar"

    override fun Scope.create(componentType: String): ToolbarState = ToolbarState(
        title = prop("title").asString() ?: "",
    )

}

data class ToolbarState(
    val title: String
)