package ru.javersingleton.bdui.v6

interface BeduinContext {

    fun inflateComponentState(componentType: String): ComponentLambda

    fun inflateComponentFields(componentType: String): Map<String, Any>

}