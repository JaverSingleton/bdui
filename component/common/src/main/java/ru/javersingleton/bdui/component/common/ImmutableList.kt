package ru.javersingleton.bdui.component.common

import androidx.compose.runtime.Immutable

@Immutable
data class ImmutableList<T>(
    val value: List<T>,
)