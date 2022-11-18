package ru.javersingleton.bdui.core.field

import ru.javersingleton.bdui.core.Lambda
import ru.javersingleton.bdui.core.Value

data class EmptyField(
    override val id: String
) : Field<Any?> {

    override fun resolve(
        scope: Lambda.Scope,
        args: Map<String, Value<*>>
    ): Field<Any?> = scope.run {
        ResolvedField(
            id = id,
            value = rememberValue(id, null) { null },
        )
    }

    @Suppress("UNCHECKED_CAST")
    override fun mergeDeeply(targetFieldId: String, targetField: Field<*>): Field<Any?> {
        return if (targetFieldId == id) {
            if (targetField is EmptyField) {
                this
            } else {
                targetField.copyWithId(id = id) as Field<Any?>
            }
        } else {
            this
        }
    }

    override fun copyWithId(id: String): Field<Any?> = copy(id = id)

}

fun EmptyField(): EmptyField = EmptyField(id = newId())