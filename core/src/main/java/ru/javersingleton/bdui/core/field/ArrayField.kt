package ru.javersingleton.bdui.core.field

import ru.javersingleton.bdui.core.Lambda
import ru.javersingleton.bdui.core.Value

data class ArrayField(
    override val id: String,
    private val fields: List<Field<*>>
): Field<ArrayData> {

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