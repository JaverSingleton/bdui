package ru.javersingleton.bdui.component.state

import ru.javersingleton.bdui.core.ComponentState

object ToolbarComponent {

    object StateFactory : ComponentState.Factory<ToolbarState>() {

        override fun Scope.create(componentType: String): ToolbarState = ToolbarState(
            title = prop("title").asString() ?: "",
        )

    }

}

data class ToolbarState(
    val title: String
)