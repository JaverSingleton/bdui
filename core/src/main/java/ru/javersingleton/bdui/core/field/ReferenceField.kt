package ru.javersingleton.bdui.core.field

import ru.javersingleton.bdui.core.Lambda
import ru.javersingleton.bdui.core.Value


data class ReferenceField(
    override val id: String,
    override val withUserId: Boolean,
    private val refFieldName: String
) : Field<ResolvedData> {

    constructor(
        id: String,
        refFieldName: String
    ) : this(id = id, withUserId = true, refFieldName)

    constructor(
        refFieldName: String
    ) : this(id = newId(), withUserId = false, refFieldName)

    @Suppress("UNCHECKED_CAST")
    override fun resolve(scope: Lambda.Scope, args: Map<String, Value<*>>): Field<ResolvedData> = scope.run {
        val argument = rememberValue(id, args) { args[refFieldName] }
        argument.current?.let {
            ResolvedField(
                id = id,
                value = argument.current as Value<ResolvedData>,
            )
        } ?: this@ReferenceField
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