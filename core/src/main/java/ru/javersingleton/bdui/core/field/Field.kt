package ru.javersingleton.bdui.core.field

import ru.javersingleton.bdui.core.Lambda
import ru.javersingleton.bdui.core.State

interface Field {

    fun resolve(scope: Lambda.Scope, args: Map<String, State<*>>): Field?

    val id: String

}

data class ResolvedField(
    override val id: String,
    val state: State<*>,
) : Field {

    override fun resolve(scope: Lambda.Scope, args: Map<String, State<*>>): Field = this

}

fun Lambda.Scope.resolveThemselves(
    id: String,
    params: Field?,
    args: Map<String, State<*>> = mapOf()
): ResolvedField {
    if (params == null) {
        return ResolvedField(id, rememberState(id, null) { null })
    }

    if (params is ResolvedField) {
        return params
    }

    val processedParams = params.resolve(this, args)
    return if (processedParams is ResolvedField) {
        processedParams
    } else {
        val newArgs = (processedParams as StructureField).extractStates()
        if (args == newArgs) {
            throw IllegalArgumentException()
        }

        resolveThemselves(id, processedParams, newArgs)
    }
}