package ru.javersingleton.bdui.engine.meta

import ru.javersingleton.bdui.engine.field.entity.ComponentField
import ru.javersingleton.bdui.engine.field.entity.StructureField


data class MetaComponentBlueprint(
    val state: StructureField,
    val rootComponent: ComponentField
)