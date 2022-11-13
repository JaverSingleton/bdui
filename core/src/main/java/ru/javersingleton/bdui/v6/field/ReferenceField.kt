package ru.javersingleton.bdui.v6.field

import ru.javersingleton.bdui.v6.Lambda
import ru.javersingleton.bdui.v6.State


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