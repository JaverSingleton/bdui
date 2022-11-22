package ru.javersingleton.bdui.component.compose.component

import android.util.Log
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import coil.transform.CircleCropTransformation
import ru.javersingleton.bdui.component.compose.subscribeAsState
import ru.javersingleton.bdui.component.state.ButtonState
import ru.javersingleton.bdui.component.state.ImageState
import ru.javersingleton.bdui.core.Value

@Composable
fun ImageComponent(
    modifier: Modifier,
    componentState: Value<ImageState>
) {
    val state = componentState.subscribeAsState().value
    Log.d("Beduin", "OnComponentRender: componentType=Image")
    AsyncImage(
        model = state.src,
        contentDescription = state.contentDescription,
        contentScale = toContentScale(state.contentScale),
        modifier = modifier.clip(state.clip),
    )
}

private fun toContentScale(value: String) = when(value) {
    "None" -> ContentScale.None
    "Fit" -> ContentScale.Fit
    "Inside" -> ContentScale.Inside
    "Crop" -> ContentScale.Crop
    else -> throw IllegalArgumentException()
}

private fun Modifier.clip(value: String): Modifier = when(value) {
    "Circle" -> clip(CircleShape)
    else -> this
}