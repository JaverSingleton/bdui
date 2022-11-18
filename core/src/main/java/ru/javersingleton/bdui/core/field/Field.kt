package ru.javersingleton.bdui.core.field

import ru.javersingleton.bdui.core.Lambda
import ru.javersingleton.bdui.core.Value
import java.util.UUID

interface Field<T> {

    fun resolve(scope: Lambda.Scope, args: Map<String, Value<*>>): Field<T>

    fun mergeDeeply(targetFieldId: String, targetField: Field<*>): Field<T>

    fun copyWithId(id: String): Field<T>

    val id: String

}

data class ResolvedField<T>(
    override val id: String,
    val value: Value<T>,
) : Field<T> {

    override fun resolve(scope: Lambda.Scope, args: Map<String, Value<*>>): Field<T> = this

    override fun mergeDeeply(targetFieldId: String, targetField: Field<*>): Field<T> = this

    override fun copyWithId(id: String): Field<T> = copy(id = id)

}

fun Lambda.Scope.resolveThemselves(
    id: String,
    params: Field<Structure>,
    args: Map<String, Value<*>> = mapOf()
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

fun newId(): String = UUID.randomUUID().toString()