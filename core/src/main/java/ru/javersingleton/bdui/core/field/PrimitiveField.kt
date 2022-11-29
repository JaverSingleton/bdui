package ru.javersingleton.bdui.core.field

import ru.javersingleton.bdui.core.Lambda
import ru.javersingleton.bdui.core.Value

data class PrimitiveField(
    override val id: String,
    override val withUserId: Boolean,
    private val value: String,
) : Field<PrimitiveData> {

    constructor(
        id: String,
        value: String
    ) : this(id = id, withUserId = true, value)

    constructor(
        value: String
    ) : this(id = newId(), withUserId = false, value)

    override fun resolve(
        scope: Lambda.Scope,
        args: Map<String, Value<*>>
    ): Field<PrimitiveData> = scope.run {
        ResolvedField(
            id = id,
            withUserId,
            value = rememberValue(id, value) { PrimitiveData(id = id, value) },
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
    private val id: String = newId(),
    private val value: String
) : ResolvedData {

    override fun toString(): String = value

    fun toInt(): Int = value.toInt()

    fun toBoolean(): Boolean = value.toBoolean()

    fun toFloat(): Float = value.toFloat()

    override fun toField(): Field<PrimitiveData> = PrimitiveField(id = id, value)

}