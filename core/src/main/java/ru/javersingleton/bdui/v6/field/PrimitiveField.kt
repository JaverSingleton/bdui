package ru.javersingleton.bdui.v6.field

import ru.javersingleton.bdui.v6.Lambda
import ru.javersingleton.bdui.v6.State

data class PrimitiveField(
    override val id: String,
    private val value: String,
) : Field {

    override fun resolve(
        scope: Lambda.Scope,
        args: Map<String, State<*>>
    ): Field = scope.run {
        ResolvedField(
            id = id,
            state = rememberState(id, value) { value },
        )
    }

}