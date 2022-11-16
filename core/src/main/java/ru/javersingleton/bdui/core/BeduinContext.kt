package ru.javersingleton.bdui.core

import ru.javersingleton.bdui.core.field.ComponentField
import ru.javersingleton.bdui.core.field.StructureField
import ru.javersingleton.bdui.core.field.Transform

interface BeduinContext {

    fun inflateTransform(transformType: String): Transform

    fun inflateDefaultFields(componentType: String): StructureField

    fun inflateRootComponent(componentType: String): ComponentField

    fun inflateStateFactory(componentType: String): ComponentState.Factory<*>


}