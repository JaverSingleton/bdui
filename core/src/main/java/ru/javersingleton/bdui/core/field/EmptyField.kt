package ru.javersingleton.bdui.core.field

import ru.javersingleton.bdui.core.Lambda
import ru.javersingleton.bdui.core.Value

data class EmptyField(
    override val id: String,
    override val withUserId: Boolean
) : Field<EmptyData> {

    constructor(
        id: String
    ) : this(id = id, withUserId = true)

    constructor() : this(id = newId(), withUserId = false)

    override fun resolve(
        scope: Lambda.Scope,
        args: Map<String, Value<*>>
    ): Field<EmptyData> = scope.run {
        ResolvedField(
            id = id,
            value = rememberValue(id, null) { EmptyData(id = id) },
        )
    }

    @Suppress("UNCHECKED_CAST")
    override fun mergeDeeply(targetFieldId: String, targetField: Field<*>): Field<EmptyData> {
        return if (targetFieldId == id) {
            if (targetField is EmptyField) {
                this
            } else {
                targetField.copyWithId(id = id) as Field<EmptyData>
            }
        } else {
            this
        }
    }

    override fun copyWithId(id: String): Field<EmptyData> = copy(id = id)

}

data class EmptyData(val id: String) : ResolvedData {
    override fun toField(): Field<EmptyData> = EmptyField(id)
}