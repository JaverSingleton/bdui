package ru.javersingleton.bdui.core.field

import ru.javersingleton.bdui.core.Lambda
import ru.javersingleton.bdui.core.Value


data class StructureField(
    override val id: String,
    private val fields: Map<String, Field<*>>
) : Field<Structure> {

    override fun resolve(scope: Lambda.Scope, args: Map<String, Value<*>>): Field<Structure> =
        scope.run {
            val targetFields = fields.map { (name, field) -> name to field.resolve(this, args) }
            if (targetFields.hasUnresolvedFields()) {
                StructureField(id, targetFields.toMap())
            } else {
                ResolvedField(
                    id,
                    rememberValue(id, targetFields) {
                        Structure(targetFields.associate { (key, value) -> key to (value as ResolvedField) })
                    }
                )
            }
        }

    fun extractStates(): Map<String, Value<*>> =
        fields
            .filterValues { it is ResolvedField }
            .mapValues { (_, value) -> (value as ResolvedField).value }

    private fun List<Pair<String, Field<*>>>.hasUnresolvedFields(): Boolean =
        firstOrNull { it.second !is ResolvedField } != null

    fun mergeWith(targetFields: Structure?): StructureField {
        if (targetFields == null) {
            return this
        }
        return StructureField(id = id, fields = fields + targetFields.fields)
    }

    @Suppress("UNCHECKED_CAST")
    override fun mergeDeeply(targetFieldId: String, targetField: Field<*>): Field<Structure> {
        return if (targetFieldId != id) {
            val targetFields = fields.mapValues { it.value.mergeDeeply(targetFieldId, targetField) }
            if (targetFields == fields) {
                this
            } else {
                copy(fields = targetFields)
            }
        } else {
            if (targetField is StructureField) {
                val changedFields = fields + targetField.fields.mapValues { (key, value) ->
                    val currentField = fields[key]
                    currentField?.mergeDeeply(currentField.id, value)
                        ?: value
                }
                if (changedFields != fields) {
                    copy(fields = changedFields)
                } else {
                    this
                }
            } else {
                targetField.copyWithId(id = id)
            } as Field<Structure>
        }
    }

    override fun copyWithId(id: String): Field<Structure> = copy(id = id)

}

fun StructureField(
    vararg fields: Pair<String, Field<*>>
): StructureField =
    StructureField(
        id = newId(),
        fields.toMap()
    )

data class Structure(
    internal val fields: Map<String, ResolvedField<*>>
) {

    fun prop(name: String): Value<*> = fields[name]?.value ?: Value.NULL

    fun hasProp(name: String): Boolean = fields.containsKey(name)

    fun unbox(): Map<String, Value<*>> = fields.mapValues { it.value.value }

}