package ru.javersingleton.bdui.v6

import ru.javersingleton.bdui.v6.field.ComponentField
import ru.javersingleton.bdui.v6.field.StructureField
import ru.javersingleton.bdui.v6.field.Transform

interface BeduinContext {

    fun inflateTransform(transformType: String): Transform

    fun inflateDefaultFields(componentType: String): StructureField

    fun inflateRootComponent(componentType: String): ComponentField?

    fun inflateStateFactory(componentType: String): StructureField

}