package ru.javersingleton.bdui.engine.field.entity

import ru.javersingleton.bdui.engine.ArgumentsStorage
import ru.javersingleton.bdui.engine.core.Lambda
import ru.javersingleton.bdui.engine.core.Value
import ru.javersingleton.bdui.engine.core.currentQuiet
import ru.javersingleton.bdui.engine.field.*


data class StructureField(
    override val id: String,
    override val withUserId: Boolean,
    val fields: Map<String, Field<*>>
) : Field<StructureData> {

    constructor(
        id: String? = null,
        fields: Map<String, Field<*>>
    ) : this(id = id ?: newId(), withUserId = id != null, fields)

    override fun resolve(scope: Lambda.Scope, args: ArgumentsStorage): Field<StructureData> =
        scope.run {
            val targetFields = fields.map { (name, field) -> name to field.resolve(this, args) }
            if (targetFields.hasUnresolvedFields()) {
                return copy(fields = targetFields.toMap())
            }

            val dataWithUserId: MutableMap<String, Value<*>> = mutableMapOf()
            val values: MutableMap<String, Value<out ResolvedData>> = mutableMapOf()
            targetFields.forEach { (key, value) ->
                val resolvedElement = value as ResolvedField
                dataWithUserId.putAll(resolvedElement.dataWithUserId)
                values[key] = resolvedElement.value
            }
            val resultValue = rememberValue(id, targetFields) {
                StructureData(
                    id,
                    withUserId,
                    targetFields.associate { (key, value) -> key to (value as ResolvedField).value }
                )
            }
            if (withUserId) {
                dataWithUserId[id] = resultValue
            }
            return ResolvedField(
                id,
                withUserId,
                resultValue,
                dataWithUserId
            )
        }

    private fun List<Pair<String, Field<*>>>.hasUnresolvedFields(): Boolean =
        firstOrNull { it.second !is ResolvedField } != null

    fun mergeWith(targetFields: StructureData?): StructureField {
        if (targetFields == null) {
            return this
        }
        return copy(
            fields = fields + targetFields.fields.mapValues { ResolvedField(it.value) }
        )
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
    private val withUserId: Boolean,
    internal val fields: Map<String, Value<out ResolvedData>> = mapOf()
) : ResolvedData, PropertiesHolder {

    constructor(
        id: String? = null,
        fields: Map<String, Value<out ResolvedData>> = mapOf()
    ) : this(
        id = id ?: newId(),
        withUserId = id != null,
        fields,
    )

    override fun prop(name: String): Value<*> = fields[name] ?: Value.NULL

    override fun hasProp(name: String): Boolean = fields.containsKey(name)

    override fun forEach(func: (key: String) -> Unit) {
        fields.forEach { (key, _) -> func(key) }
    }

    override fun unbox(): Map<String, Value<*>> = fields

    override fun asField(): Field<StructureData> = StructureField(
        id = id.takeIf { withUserId } ?: newId(),
        withUserId = withUserId,
        fields = fields.mapValues { (_, value) ->
            value.currentQuiet<ResolvedData> { it }.asField()
        }
    )

}