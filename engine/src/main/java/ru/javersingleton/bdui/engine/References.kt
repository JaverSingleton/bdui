package ru.javersingleton.bdui.engine

import android.util.Log
import ru.javersingleton.bdui.engine.core.Lambda
import ru.javersingleton.bdui.engine.core.Value
import ru.javersingleton.bdui.engine.core.currentQuiet

class MutableReferences : References {

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

        refs.forEach { (key, value) ->
            Log.d("Beduin", "Reference: id=$key, value=${value.currentQuiet}")
        }
    }

}

interface References {

    operator fun get(refName: String): Value<Value<*>>

}