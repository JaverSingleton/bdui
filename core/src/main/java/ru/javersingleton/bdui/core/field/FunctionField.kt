package ru.javersingleton.bdui.core.field

import ru.javersingleton.bdui.core.Lambda
import ru.javersingleton.bdui.core.Value

data class FunctionField(
    override val id: String,
    override val withUserId: Boolean,
    private val functionType: String,
    private val params: Field<StructureData>,
) : Field<ResolvedData> {

    constructor(
        id: String,
        functionType: String,
        params: Field<StructureData>
    ) : this(id = id, withUserId = true, functionType, params)

    constructor(
        functionType: String,
        params: Field<StructureData>
    ) : this(id = newId(), withUserId = false, functionType, params)

    override fun resolve(scope: Lambda.Scope, args: Map<String, Value<*>>): Field<ResolvedData> =
        scope.run {
            val params = params.resolve(this, args)
            if (params !is ResolvedField<StructureData>) {
                return FunctionField(
                    id,
                    functionType,
                    params,
                )
            }

            val function = rememberValue("$id@function", functionType) {
                inflateFunction(functionType)
            }

            @Suppress("UNCHECKED_CAST")
            ResolvedField(
                id = id,
                value = function.current?.calculate(
                    scope,
                    id,
                    params.value.current {
                        StructureData(id = it.id)
                    }
                ) ?: throw IllegalArgumentException("Function $functionType not found")
            )
        }

    @Suppress("UNCHECKED_CAST")
    override fun mergeDeeply(targetFieldId: String, targetField: Field<*>): Field<ResolvedData> {
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
            } as Field<ResolvedData>
        }
    }

    override fun copyWithId(id: String): Field<ResolvedData> = copy(id = id)

}

