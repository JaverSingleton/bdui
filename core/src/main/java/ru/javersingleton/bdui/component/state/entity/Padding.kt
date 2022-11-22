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
            structure.run {
                Padding(
                    start = prop("start").asInt() ?: 0,
                    end = prop("end").asInt() ?: 0,
                    bottom = prop("bottom").asInt() ?: 0,
                    top = prop("top").asInt() ?: 0,
                )
            }
        }

    }

}