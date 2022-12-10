package ru.javersingleton.bdui.engine.field.entity

import ru.javersingleton.bdui.engine.References
import ru.javersingleton.bdui.engine.core.Lambda
import ru.javersingleton.bdui.engine.field.Field
import ru.javersingleton.bdui.engine.field.ResolvedData
import ru.javersingleton.bdui.engine.field.ResolvedField
import ru.javersingleton.bdui.engine.field.newId

data class EmptyField(
    override val id: String,
    override val withUserId: Boolean
) : Field<EmptyData> {

    constructor(
        id: String? = null
    ) : this(id = id ?: newId(), withUserId = id != null)

    override fun resolve(
        scope: Lambda.Scope,
        args: References
    ): Field<EmptyData> = scope.run {
        val resultValue = rememberValue(id, null) { EmptyData(id, withUserId) }
        ResolvedField(
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

data class EmptyData(
    val id: String,
    val withUserId: Boolean
) : ResolvedData {

    @Suppress("unused")
    constructor(
        id: String? = null
    ) : this(
        id = id ?: newId(),
        withUserId = id != null
    )

    override fun asField(): Field<EmptyData> = EmptyField(id)

}