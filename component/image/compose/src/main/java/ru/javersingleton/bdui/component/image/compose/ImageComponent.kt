package ru.javersingleton.bdui.component.image.compose

import android.util.Log
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import ru.javersingleton.bdui.engine.core.Value
import ru.javersingleton.bdui.render.compose.ComponentRender
import ru.javersingleton.bdui.render.compose.subscribeAsState

object ImageComponent : ComponentRender<ImageState>(ImageStateFactory) {

    override val type: String = "Image"

    @Composable
    override fun Render(
        modifier: Modifier,
        stateValue: Value<ImageState>
    ) {
        val state = stateValue.subscribeAsState().value
        Log.d("Beduin", "OnComponentRender: componentType=Image")
        AsyncImage(
            model = state.src,
            contentDescription = state.contentDescription,
            contentScale = toContentScale(state.contentScale),
            modifier = modifier.clip(state.clip),
        )
    }

}

private fun toContentScale(value: String) = when (value) {
    "None" -> ContentScale.None
    "Fit" -> ContentScale.Fit
    "Inside" -> ContentScale.Inside
    "Crop" -> ContentScale.Crop
    else -> throw IllegalArgumentException()
}

private fun Modifier.clip(value: String): Modifier = when (value) {
    "Circle" -> clip(CircleShape)
    else -> this
}