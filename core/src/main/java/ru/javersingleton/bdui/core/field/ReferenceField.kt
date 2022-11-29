package ru.javersingleton.bdui.core.field

import ru.javersingleton.bdui.core.Lambda
import ru.javersingleton.bdui.core.References
import ru.javersingleton.bdui.core.Value


data class ReferenceField(
    override val id: String,
    override val withUserId: Boolean,
    private val refFieldName: String
) : Field<ResolvedData> {

    constructor(
        id: String? = null,
        refFieldName: String
    ) : this(id = id ?: newId(), withUserId = id != null, refFieldName)

    @Suppress("UNCHECKED_CAST")
    override fun resolve(scope: Lambda.Scope, args: References): Field<ResolvedData> = scope.run {
        val resultValue: Value<ResolvedData> = rememberValue(id, args) {
            val valueContainer = args[refFieldName].current
                ?: throw IllegalArgumentException("Container of Arg $refFieldName not found")
            valueContainer.current { it }
        }
        return ResolvedField(
            id = id,
            withUserId = withUserId,
            value = resultValue,
            dataWithUserId = if (withUserId) {
                mapOf(id to resultValue)
            } else {
                mapOf()
            }
        )
    }

    @Suppress("UNCHECKED_CAST")
    override fun mergeDeeply(targetFieldId: String, targetField: Field<*>): Field<ResolvedData> =
        if (targetFieldId == id) {
            if (targetField is ReferenceField) {
                copy(refFieldName = targetField.refFieldName)
            } else {
                targetField.copyWithId(id) as Field<ResolvedData>
            }
        } else {
            this
        }

    override fun copyWithId(id: String): Field<ResolvedData> = copy(id = id)

}