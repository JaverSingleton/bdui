package ru.javersingleton.bdui.core.field

import ru.javersingleton.bdui.core.Lambda
import ru.javersingleton.bdui.core.State

interface Field<T> {

    fun resolve(scope: Lambda.Scope, args: Map<String, State<*>>): Field<T>

    val id: String

}

data class ResolvedField<T>(
    override val id: String,
    val state: State<*>,
) : Field<T> {

    override fun resolve(scope: Lambda.Scope, args: Map<String, State<*>>): Field<T> = this

}

fun Lambda.Scope.resolveThemselves(
    id: String,
    params: Field<Structure>,
    args: Map<String, State<*>> = mapOf()
): ResolvedField<Structure> {

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