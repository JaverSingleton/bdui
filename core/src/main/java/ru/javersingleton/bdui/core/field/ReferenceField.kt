package ru.javersingleton.bdui.core.field

import ru.javersingleton.bdui.core.Lambda
import ru.javersingleton.bdui.core.State


data class ReferenceField(
    private val refFieldName: String,
    override val id: String
) : Field {

    override fun resolve(scope: Lambda.Scope, args: Map<String, State<*>>): Field = scope.run {
        val argument = rememberState(id, args) { args[refFieldName] }
        return if (argument.value != null) {
            ResolvedField(
                id = id,
                state = argument,
            )
        } else {
            this@ReferenceField
        }
    }

}