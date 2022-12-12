package ru.javersingleton.bdui.engine

import ru.javersingleton.bdui.engine.core.Lambda
import ru.javersingleton.bdui.engine.core.Value

class MutableArgumentsStorage : ArgumentsStorage {

    private var values: Map<String, Value<Value<*>>> = mapOf()

    override operator fun get(refName: String): Value<Value<*>> =
        values[refName] ?: throw IllegalArgumentException("Field $refName not found")

    fun replace(
        scope: Lambda.Scope,
        refs: Map<String, Value<*>>
    ) = scope.run {
        val targetValues = mutableMapOf<String, Value<Value<*>>>()
        refs.forEach { (key, value) ->
            val resultValue = rememberValue("$key@referenceOn", value) { value }
            targetValues[key] = resultValue
        }

        values = targetValues
    }

}

interface ArgumentsStorage {

    operator fun get(refName: String): Value<Value<*>>

}