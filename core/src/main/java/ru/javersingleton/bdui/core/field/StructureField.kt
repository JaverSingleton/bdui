package ru.javersingleton.bdui.core.field

import ru.javersingleton.bdui.core.Lambda
import ru.javersingleton.bdui.core.State


data class StructureField(
    override val id: String,
    private val fields: Map<String, Field?>
) : Field {

    override fun resolve(scope: Lambda.Scope, args: Map<String, State<*>>): Field =
        scope.run {
            val targetFields = fields.map { (name, field) -> name to field?.resolve(this, args) }
            if (targetFields.hasUnresolvedFields()) {
                StructureField(id, targetFields.toMap())
            } else {
                ResolvedField(
                    id,
                    rememberState(id, targetFields) {
                        Structure(targetFields.associate { (key, value) -> key to (value as ResolvedField) })
                    }
                )
            }
        }

    fun extractStates(): Map<String, State<*>> =
        fields
            .filterValues { it is ResolvedField }
            .mapValues { (_, value) -> (value as ResolvedField).state }

    private fun List<Pair<String, Field?>>.hasUnresolvedFields(): Boolean =
        firstOrNull { it.second !is ResolvedField? } != null

    fun mergeWith(targetFields: Structure?): StructureField {
        if (targetFields == null) {
            return this
        }
        return StructureField(id = id, fields = fields + targetFields.fields)
    }

}

data class Structure(
    internal val fields: Map<String, ResolvedField>
) {

    fun prop(name: String): State<*> = fields[name]?.state!!

    fun hasProp(name: String): Boolean = fields.containsKey(name)

    fun unbox(): Map<String, State<*>> = fields.mapValues { it.value.state }

}