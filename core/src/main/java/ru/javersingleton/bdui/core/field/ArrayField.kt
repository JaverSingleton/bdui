package ru.javersingleton.bdui.core.field

import ru.javersingleton.bdui.core.Lambda
import ru.javersingleton.bdui.core.Value
import ru.javersingleton.bdui.core.currentQuiet

data class ArrayField(
    override val id: String,
    override val withUserId: Boolean,
    private val fields: List<Field<*>>,
) : Field<ArrayData> {

    constructor(
        id: String,
        fields: List<Field<*>>
    ): this(id = id, withUserId = true, fields)

    constructor(
        fields: List<Field<*>>
    ): this(id = newId(), withUserId = false, fields)

    override fun resolve(scope: Lambda.Scope, args: Map<String, Value<*>>): Field<ArrayData> =
        scope.run {
            val targetFields = fields.map { field -> field.resolve(this, args) }
            if (targetFields.hasUnresolvedFields()) {
                ArrayField(id, targetFields)
            } else {
                ResolvedField(
                    id,
                    rememberValue(id, targetFields) {
                        ArrayData(id, targetFields.map { (it as ResolvedField<*>).value }.toList())
                    }
                )
            }
        }

    private fun List<Field<*>>.hasUnresolvedFields(): Boolean =
        firstOrNull { it !is ResolvedField } != null

    @Suppress("UNCHECKED_CAST")
    override fun mergeDeeply(targetFieldId: String, targetField: Field<*>): Field<ArrayData> {
        return if (targetFieldId != id) {
            val targetFields = fields.map { it.mergeDeeply(targetFieldId, targetField) }
            if (targetFields == fields) {
                this
            } else {
                copy(fields = targetFields)
            }
        } else {
            if (targetField is ArrayField) {
                val changedFields = fields.map { value ->
                    targetField.fields.firstOrNull { it.id == value.id }
                        ?.let { targetFieldElement ->
                            value.mergeDeeply(
                                value.id,
                                targetFieldElement
                            )
                        }
                        ?: value
                }
                if (changedFields == fields) {
                    this
                } else {
                    copy(fields = changedFields)
                }
            } else {
                targetField.copyWithId(id = id)
            } as Field<ArrayData>
        }
    }

    override fun copyWithId(id: String): Field<ArrayData> = copy(id = id)

}

data class ArrayData(
    val id: String,
    internal val fields: List<Value<out ResolvedData>>
): ResolvedData {

    operator fun get(index: Int): Value<*> = fields[index]

    val size get() = fields.size

    override fun toField(): Field<ArrayData> = ArrayField(
        id = id,
        fields = fields.map { it.currentQuiet<ResolvedData> { empty -> empty }.toField() }
    )

}