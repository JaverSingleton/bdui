package ru.javersingleton.bdui.engine.meta

import ru.javersingleton.bdui.engine.field.ComponentField
import ru.javersingleton.bdui.engine.field.StructureField


data class MetaComponentBlueprint(
    val state: StructureField,
    val rootComponent: ComponentField
)