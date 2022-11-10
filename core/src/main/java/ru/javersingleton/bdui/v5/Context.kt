package ru.javersingleton.bdui.v5

interface BeduinContext {

    fun inflateStateField(componentType: String): StructureField

    fun inflateComponent(componentType: String): Component

}