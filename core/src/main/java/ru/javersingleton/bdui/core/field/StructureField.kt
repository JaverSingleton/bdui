package ru.javersingleton.bdui.core.field

import ru.javersingleton.bdui.core.Lambda
import ru.javersingleton.bdui.core.Value
import ru.javersingleton.bdui.core.currentQuiet


data class StructureField(
    override val id: String,
    override val withUserId: Boolean,
    val fields: Map<String, Field<*>>
) : Field<StructureData> {

    constructor(
        id: String,
        fields: Map<String, Field<*>>
    ): this(id = id, withUserId = true, fields)

    constructor(
        fields: Map<String, Field<*>>
    ): this(id = newId(), withUserId = false, fields)

    override fun resolve(scope: Lambda.Scope, args: Map<String, Value<*>>): Field<StructureData> =
        scope.run {
            val targetFields = fields.map { (name, field) -> name to field.resolve(this, args) }
            if (targetFields.hasUnresolvedFields()) {
                StructureField(id, targetFields.toMap())
            } else {
                ResolvedField(
                    id,
                    withUserId,
                    rememberValue(id, targetFields) {
                        StructureData(
                            id = id,
                            targetFields.associate { (key, value) -> key to (value as ResolvedField).value }
                        )
                    }
                )
            }
        }

    fun resolveThemselves(scope: Lambda.Scope): Field<StructureData> =
        scope.run {
            val args: MutableMap<String, Value<*>> = mutableMapOf()
            val resolvedFields: MutableMap<String, ResolvedField<*>> = mutableMapOf()
            val unresolvedFields: MutableMap<String, Field<*>> = fields.toMutableMap()
            var lastUnresolvedFields: Map<String, Field<*>> = fields
            while (unresolvedFields.isNotEmpty()) {
                val unresolvedFieldsIterator = unresolvedFields.iterator()
                while (unresolvedFieldsIterator.hasNext()) {
                    val targetField = unresolvedFieldsIterator.next()
                    val processedField = targetField.value.resolve(scope, args)
                    if (processedField is ResolvedField) {
                        unresolvedFieldsIterator.remove()
                        resolvedFields[targetField.key] = processedField
                        args[targetField.key] = processedField.value
                    }
                }
                if (lastUnresolvedFields == unresolvedFields) {
                    return@run this@StructureField
                }
                lastUnresolvedFields = unresolvedFields.toMap()
            }
            ResolvedField(
                id,
                withUserId,
                rememberValue(id, resolvedFields) {
                    StructureData(
                        id = id,
                        resolvedFields.mapValues { it.value.value }
                    )
                }
            )
        }

    fun extractStates(): Map<String, Value<*>> =
        fields
            .filterValues { it is ResolvedField }
            .mapValues { (_, value) -> (value as ResolvedField).value }

    private fun List<Pair<String, Field<*>>>.hasUnresolvedFields(): Boolean =
        firstOrNull { it.second !is ResolvedField } != null

    fun mergeWith(targetFields: StructureData?): StructureField {
        if (targetFields == null) {
            return this
        }
        return StructureField(id = id, fields = fields + targetFields.fields.mapValues { ResolvedField(it.value) })
    }

    @Suppress("UNCHECKED_CAST")
    override fun mergeDeeply(targetFieldId: String, targetField: Field<*>): Field<StructureData> {
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
            } as Field<StructureData>
        }
    }

    override fun copyWithId(id: String): Field<StructureData> = copy(id = id)

}

data class StructureData(
    val id: String,
    internal val fields: Map<String, Value<out ResolvedData>> = mapOf()
): ResolvedData {

    fun prop(name: String): Value<*> = fields[name] ?: Value.NULL

    fun hasProp(name: String): Boolean = fields.containsKey(name)

    fun forEach(func: (key: String) -> Unit) {
        fields.forEach { (key, _) -> func(key) }
    }

    fun unbox(): Map<String, Value<*>> = fields.mapValues { it.value }

    override fun toField(): Field<StructureData> = StructureField(
        id = id,
        fields = fields.mapValues { (_, value) -> value.currentQuiet<ResolvedData> { it }.toField() }
    )

}