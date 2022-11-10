package ru.javersingleton.bdui.v6.field

import ru.javersingleton.bdui.v6.ConstState
import ru.javersingleton.bdui.v6.Lambda
import ru.javersingleton.bdui.v6.State

class StructureField(
    override val id: String,
    private val fields: Map<String, Field?>
) : Field {

    override fun resolve(scope: Lambda.Scope): Field = scope.run {
        val targetFields = fields.map { (name, field) -> name to field?.resolve(this) }
        if (targetFields.hasUnresolvedFields()) {
            StructureField(id, targetFields.toMap())
        } else {
            val states = targetFields
                .map { (name, field) -> name to (field as ResolvedField).state }
                .toMap()
            ResolvedField(id, ConstState(Structure(states)))
        }
    }

    private fun List<Pair<String, Field?>>.hasUnresolvedFields(): Boolean =
        firstOrNull { it.second !is ResolvedField? } != null

}

class Structure(
    private val states: Map<String, State>
) {

    fun structureField(name: String): State = states[name]!!

    fun unbox(): Map<String, State> = states

}