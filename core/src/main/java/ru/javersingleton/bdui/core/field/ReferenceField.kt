package ru.javersingleton.bdui.core.field

import ru.javersingleton.bdui.core.Lambda
import ru.javersingleton.bdui.core.Value


data class ReferenceField(
    private val refFieldName: String,
    override val id: String
) : Field<Any?> {

    @Suppress("UNCHECKED_CAST")
    override fun resolve(scope: Lambda.Scope, args: Map<String, Value<*>>): Field<Any?> = scope.run {
        val argument = rememberValue(id, args) { args[refFieldName] }
        argument.current?.let {
            ResolvedField(
                id = id,
                value = argument.current as Value<Any?>,
            )
        } ?: this@ReferenceField
    }

}