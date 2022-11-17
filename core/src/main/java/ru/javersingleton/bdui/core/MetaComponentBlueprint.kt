package ru.javersingleton.bdui.core

import ru.javersingleton.bdui.core.field.ComponentField
import ru.javersingleton.bdui.core.field.StructureField


data class MetaComponentBlueprint(
    val state: StructureField,
    val rootComponent: ComponentField
)