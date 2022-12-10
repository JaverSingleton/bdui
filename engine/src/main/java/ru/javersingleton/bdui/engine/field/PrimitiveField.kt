package ru.javersingleton.bdui.engine.field

import ru.javersingleton.bdui.engine.References
import ru.javersingleton.bdui.engine.core.Lambda

data class PrimitiveField(
    override val id: String,
    override val withUserId: Boolean,
    val value: String,
) : Field<PrimitiveData> {

    constructor(
        id: String? = null,
        value: String
    ) : this(id = id ?: newId(), withUserId = id != null, value)

    override fun resolve(
        scope: Lambda.Scope,
        args: References
    ): Field<PrimitiveData> = scope.run {
        val resultValue = rememberValue(id, value) {
            PrimitiveData(
                id = id,
                withUserId = withUserId,
                value
            )
        }
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
    override fun mergeDeeply(targetFieldId: String, targetField: Field<*>): Field<PrimitiveData> =
        if (targetFieldId == id) {
            if (targetField is PrimitiveField) {
                copy(value = targetField.value)
            } else {
                targetField.copyWithId(id) as Field<PrimitiveData>
            }
        } else {
            this
        }

    override fun copyWithId(id: String): Field<PrimitiveData> = copy(id = id)

}

data class PrimitiveData(
    val id: String,
    val withUserId: Boolean,
    val value: String
) : ResolvedData {

    constructor(
        id: String? = null,
        value: String
    ) : this(
        id = id ?: newId(),
        withUserId = id != null,
        value
    )

    fun asString(): String = value

    fun asInt(): Int = value.toInt()

    fun asBoolean(): Boolean = value.toBoolean()

    @Suppress("unused")
    fun asFloat(): Float = value.toFloat()

    override fun asField(): Field<PrimitiveData> = PrimitiveField(
        id = id.takeIf { withUserId } ?: newId(),
        withUserId = withUserId,
        value
    )

}