package ru.javersingleton.bdui.core.field

import ru.javersingleton.bdui.core.Lambda
import ru.javersingleton.bdui.core.Value

data class PrimitiveField(
    override val id: String = newId(),
    private val value: String,
) : Field<Primitive> {

    override fun resolve(
        scope: Lambda.Scope,
        args: Map<String, Value<*>>
    ): Field<Primitive> = scope.run {
        ResolvedField(
            id = id,
            value = rememberValue(id, value) { Primitive(value) },
        )
    }

    @Suppress("UNCHECKED_CAST")
    override fun mergeDeeply(targetFieldId: String, targetField: Field<*>): Field<Primitive> =
        if (targetFieldId == id) {
            if (targetField is PrimitiveField) {
                copy(value = targetField.value)
            } else {
                targetField.copyWithId(id) as Field<Primitive>
            }
        } else {
            this
        }

    override fun copyWithId(id: String): Field<Primitive> = copy(id = id)

}

fun PrimitiveField(
    value: String
): PrimitiveField =
    PrimitiveField(
        id = newId(),
        value
    )

data class Primitive(private val value: String) {

    override fun toString(): String = value

    fun toInt(): Int = value.toInt()

    fun toFloat(): Float = value.toFloat()

}