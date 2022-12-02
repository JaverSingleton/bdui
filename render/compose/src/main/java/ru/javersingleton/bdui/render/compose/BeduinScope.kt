package ru.javersingleton.bdui.render.compose

import androidx.compose.runtime.*

@Composable
fun BeduinScope(
    context: BeduinComposeContext,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalBeduinContext provides context, content = content)
}

val LocalBeduinContext: ProvidableCompositionLocal<BeduinComposeContext> =
    compositionLocalOf(structuralEqualityPolicy()) {
        throw IllegalStateException("Use Beduin in BeduinScope")
    }