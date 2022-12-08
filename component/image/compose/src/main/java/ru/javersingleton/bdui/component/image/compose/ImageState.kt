package ru.javersingleton.bdui.component.image.compose

import ru.javersingleton.bdui.engine.ComponentStateFactory

object ImageStateFactory : ComponentStateFactory<ImageState>() {

    override val type: String = "Image"

    override fun Scope.create(componentType: String): ImageState = ImageState(
        src = prop("src").asString() ?: "",
        contentDescription = prop("contentDescription").asString() ?: "",
        contentScale = prop("contentScale").asString() ?: "None",
        clip = prop("clip").asString() ?: ""
    )

}

data class ImageState(
    val src: String,
    val contentDescription: String,
    val contentScale: String,
    val clip: String,
)