package ru.javersingleton.bdui.core.field

import ru.javersingleton.bdui.core.Lambda
import ru.javersingleton.bdui.core.State

data class ArrayField(
    override val id: String,
    private val fields: List<Field?>
): Field {

    override fun resolve(scope: Lambda.Scope, args: Map<String, State<*>>): Field =
        scope.run {
            val targetFields = fields.map { field -> field?.resolve(this, args) }
            if (targetFields.hasUnresolvedFields()) {
                ArrayField(id, targetFields)
            } else {
                ResolvedField(
                    id,
                    rememberState(id, targetFields) {
                        ArrayData(targetFields.map { it as ResolvedField }.toList())
                    }
                )
            }
        }

    private fun List<Field?>.hasUnresolvedFields(): Boolean =
        firstOrNull { it !is ResolvedField? } != null

}

data class ArrayData(
    internal val fields: List<ResolvedField>
) {

    operator fun get(index: Int): State<*> = fields[index].state

    val size get() = fields.size

}