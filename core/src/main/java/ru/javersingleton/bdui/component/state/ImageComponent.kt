package ru.javersingleton.bdui.component.state

import ru.javersingleton.bdui.core.ComponentState

object ImageComponent {

    object StateFactory : ComponentState.Factory<ImageState>() {

        override fun Scope.create(componentType: String): ImageState = ImageState(
            src = prop("src").asString() ?: "",
            contentDescription = prop("contentDescription").asString() ?: "",
            contentScale = prop("contentScale").asString() ?: "None",
            clip = prop("clip").asString() ?: ""
        )

    }

}

data class ImageState(
    val src: String,
    val contentDescription: String,
    val contentScale: String,
    val clip: String,
)