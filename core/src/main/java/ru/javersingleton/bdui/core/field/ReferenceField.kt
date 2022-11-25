package ru.javersingleton.bdui.core.field

import ru.javersingleton.bdui.core.Lambda
import ru.javersingleton.bdui.core.Value


data class ReferenceField(
    override val id: String = newId(),
    private val refFieldName: String
) : Field<ResolvedData> {

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

fun ReferenceField(
    refFieldName: String
): ReferenceField =
    ReferenceField(
        id = newId(),
        refFieldName = refFieldName
    )