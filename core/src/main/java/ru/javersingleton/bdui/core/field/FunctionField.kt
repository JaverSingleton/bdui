package ru.javersingleton.bdui.core.field

import ru.javersingleton.bdui.core.Lambda
import ru.javersingleton.bdui.core.Value

data class FunctionField(
    override val id: String = newId(),
    private val params: Field<Structure>,
    private val functionType: String,
) : Field<Any?> {

    override fun resolve(scope: Lambda.Scope, args: Map<String, Value<*>>): Field<Any?> = scope.run {
        val params = params.resolve(this, args)
        if (params !is ResolvedField) {
            return FunctionField(
                id,
                params,
                functionType,
            )
        }

        val function = rememberValue("$id@function", functionType) {
            inflateFunction(functionType)
        }

        ResolvedField(
            id = id,
            value = function.current.calculate(scope, id, params.value.current)
        )
    }

    @Suppress("UNCHECKED_CAST")
    override fun mergeDeeply(targetFieldId: String, targetField: Field<*>): Field<Any?> {
        return if (targetFieldId != id) {
            val targetParams = params.mergeDeeply(targetFieldId, targetField)
            if (targetParams != params) {
                this.copy(params = targetParams)
            } else {
                this
            }
        } else {
            if (targetField is FunctionField) {
                params.mergeDeeply(params.id, targetField.params)
            } else {
                targetField.copyWithId(id = id)
            } as Field<Any?>
        }
    }

    override fun copyWithId(id: String): Field<Any?> = copy(id = id)

}

fun FunctionField(
    type: String,
    vararg fields: Pair<String, Field<*>>,
    id: String = newId(),
): FunctionField  =
    FunctionField(
        id = id,
        functionType = type,
        params = StructureField(*fields)
    )

interface Function {

    fun calculate(scope: Lambda.Scope, id: String, params: Structure): Value<Any?>

}