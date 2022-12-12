package ru.javersingleton.bdui.engine.field.entity

import ru.javersingleton.bdui.engine.References
import ru.javersingleton.bdui.engine.core.Lambda
import ru.javersingleton.bdui.engine.field.Field
import ru.javersingleton.bdui.engine.field.ResolvedData
import ru.javersingleton.bdui.engine.field.ResolvedField
import ru.javersingleton.bdui.engine.field.newId

data class FunctionField(
    override val id: String,
    override val withUserId: Boolean,
    private val functionType: String,
    private val params: Field<StructureData>,
) : Field<ResolvedData> {

    constructor(
        id: String? = null,
        functionType: String,
        params: Field<StructureData>
    ) : this(id = id ?: newId(), withUserId = id != null, functionType, params)

    override fun resolve(scope: Lambda.Scope, args: References): Field<ResolvedData> =
        scope.run {
            val params = params.resolve(this, args)
            if (params !is ResolvedField<StructureData>) {
                return copy(params = params)
            }

            val function = rememberValue("$id@function", functionType) {
                inflateFunction(functionType)
            }

            val resultValue = function.current?.calculate(
                scope,
                id,
                params.value.current {
                    StructureData(
                        id = it.id,
                        withUserId = it.withUserId
                    )
                }
            ) ?: throw IllegalArgumentException("Function $functionType not found")
            @Suppress("UNCHECKED_CAST")
            (ResolvedField(
        id = id,
        withUserId = withUserId,
        value = resultValue,
        dataWithUserId = if (withUserId) {
            mapOf(id to resultValue)
        } else {
            mapOf()
        }
    ))
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

