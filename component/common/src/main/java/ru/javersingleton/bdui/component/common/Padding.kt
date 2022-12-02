package ru.javersingleton.bdui.component.common

import ru.javersingleton.bdui.engine.ComponentStateFactory
import ru.javersingleton.bdui.engine.field.StructureData

data class Padding(
    val start: Int,
    val end: Int,
    val bottom: Int,
    val top: Int,
) {

    companion object {

        fun create(
            scope: ComponentStateFactory.Scope,
            structure: StructureData,
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