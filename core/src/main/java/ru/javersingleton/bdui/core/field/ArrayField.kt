package ru.javersingleton.bdui.core.field

import ru.javersingleton.bdui.core.Lambda
import ru.javersingleton.bdui.core.Value

data class ArrayField(
    override val id: String = newId(),
    private val fields: List<Field<*>>
) : Field<ArrayData> {

    override fun resolve(scope: Lambda.Scope, args: Map<String, Value<*>>): Field<ArrayData> =
        scope.run {
            val targetFields = fields.map { field -> field.resolve(this, args) }
            if (targetFields.hasUnresolvedFields()) {
                ArrayField(id, targetFields)
            } else {
                ResolvedField(
                    id,
                    rememberValue(id, targetFields) {
                        ArrayData(targetFields.map { it as ResolvedField }.toList())
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

fun ArrayField(
    vararg fields: Field<*>
): ArrayField = ArrayField(
    id = newId(),
    fields = fields.toList()
)

data class ArrayData(
    internal val fields: List<ResolvedField<*>>
) {

    operator fun get(index: Int): Value<*> = fields[index].value

    val size get() = fields.size

}