package ru.javersingleton.bdui.engine.field.entity

import ru.javersingleton.bdui.engine.ArgumentsStorage
import ru.javersingleton.bdui.engine.core.Scope
import ru.javersingleton.bdui.engine.core.Value
import ru.javersingleton.bdui.engine.core.currentQuiet
import ru.javersingleton.bdui.engine.field.Field
import ru.javersingleton.bdui.engine.field.ResolvedData
import ru.javersingleton.bdui.engine.field.ResolvedField
import ru.javersingleton.bdui.engine.field.newId

data class ArrayField(
    override val id: String,
    override val withUserId: Boolean,
    val fields: List<Field<*>>,
) : Field<ArrayData> {

    constructor(
        id: String? = null,
        fields: List<Field<*>>
    ) : this(
        id = id ?: newId(),
        withUserId = id != null,
        fields
    )

    override fun resolve(scope: Scope, args: ArgumentsStorage): Field<ArrayData> =
        scope.run {
            val targetFields = fields.map { field -> field.resolve(this, args) }
            if (targetFields.hasUnresolvedFields()) {
                return copy(fields = targetFields)
            }

            val dataWithUserId: MutableMap<String, Value<*>> = mutableMapOf()
            val values: MutableList<Value<out ResolvedData>> = mutableListOf()
            targetFields.forEach {
                val resolvedElement = it as ResolvedField
                dataWithUserId.putAll(resolvedElement.dataWithUserId)
                values.add(resolvedElement.value)
            }
            val resultValue = rememberValue(id, targetFields) {
                ArrayData(
                    id,
                    withUserId,
                    targetFields.map { (it as ResolvedField<*>).value }.toList()
                )
            }
            if (withUserId) {
                dataWithUserId[id] = resultValue
            }
            ResolvedField(
                id,
                withUserId,
                resultValue,
                dataWithUserId
            )
        }

    private fun List<Field<*>>.hasUnresolvedFields(): Boolean =
        firstOrNull { it !is ResolvedField } != null

    @Suppress("UNCHECKED_CAST")
    override fun mergeDeeply(targetFieldId: String, targetField: Field<*>): Field<ArrayData> {
        if (targetFieldId != id) {
            val targetFields = fields.map {
                it.mergeDeeply(
                    targetFieldId = targetFieldId,
                    targetField = targetField
                )
            }
            return if (targetFields == fields) {
                this
            } else {
                copy(fields = targetFields)
            }
        }

        return if (targetField is ArrayField) {
            //TODO Написать нормальный мёрдж массивов с сохранением позиций
            var changedFields = fields.map { value ->
                targetField.fields
                    .firstOrNull { it.id == value.id }
                    ?.let {
                        value.mergeDeeply(
                            targetFieldId = value.id,
                            targetField = it
                        )
                    } ?: value
            }
            changedFields = changedFields + targetField.fields.filter { element1 ->
                changedFields.firstOrNull { element2 -> element1.id == element2.id } == null
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

    override fun copyWithId(id: String): Field<ArrayData> = copy(id = id)

}

data class ArrayData(
    val id: String,
    val withUserId: Boolean,
    val fields: List<Value<out ResolvedData>>
) : ResolvedData {

    constructor(
        id: String? = null,
        fields: List<Value<out ResolvedData>>
    ) : this(
        id = id ?: newId(),
        withUserId = id != null,
        fields
    )

    operator fun get(index: Int): Value<*> = fields[index]

    val size get() = fields.size

    override fun asField(): Field<ArrayData> = ArrayField(
        id = id.takeIf { withUserId } ?: newId(),
        withUserId = withUserId,
        fields = fields.map { it.currentQuiet<ResolvedData> { empty -> empty }.asField() }
    )

}