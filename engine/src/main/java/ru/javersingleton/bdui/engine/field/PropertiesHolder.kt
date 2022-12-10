package ru.javersingleton.bdui.engine.field

import ru.javersingleton.bdui.engine.core.Value

interface PropertiesHolder {

    fun prop(name: String): Value<*>

    fun hasProp(name: String): Boolean

    fun forEach(func: (key: String) -> Unit)

    fun unbox(): Map<String, Value<*>>

}