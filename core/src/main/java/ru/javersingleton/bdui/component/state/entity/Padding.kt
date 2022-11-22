package ru.javersingleton.bdui.component.state.entity

import ru.javersingleton.bdui.core.ComponentState
import ru.javersingleton.bdui.core.field.Structure

data class Padding(
    val start: Int,
    val end: Int,
    val bottom: Int,
    val top: Int,
) {

    companion object {

        fun create(
            scope: ComponentState.Factory.Scope,
            structure: Structure,
        ): Padding = scope.run {
            Padding(
                start = structure.prop("start").asInt() ?: 0,
                end = structure.prop("end").asInt() ?: 0,
                bottom = structure.prop("bottom").asInt() ?: 0,
                top = structure.prop("top").asInt() ?: 0,
            )
        }

    }

}