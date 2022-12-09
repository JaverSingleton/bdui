package ru.javersingleton.bdui.engine.field

import ru.javersingleton.bdui.engine.References
import ru.javersingleton.bdui.engine.core.Lambda
import ru.javersingleton.bdui.engine.core.Value
import ru.javersingleton.bdui.engine.core.currentQuiet


data class ReferenceField(
    override val id: String,
    override val withUserId: Boolean,
    private val refFieldName: String
) : Field<ResolvedData> {

    constructor(
        id: String? = null,
        refFieldName: String
    ) : this(id = id ?: newId(), withUserId = id != null, refFieldName)

    @Suppress("UNCHECKED_CAST")
    override fun resolve(scope: Lambda.Scope, args: References): Field<ResolvedData> = scope.run {
        val resultValue: Value<ResolvedData> = rememberValue(id, setOf(args, refFieldName)) {
            val refPath = refFieldName.split(".")
            var result: Value<*>? = args[refPath[0]].current
            for (i in (1 until refPath.size)) {
                val refNode = refPath[i]
                // TODO Вынести в абстракцию
                val structure = when(val data = result?.currentQuiet) {
                    is ComponentData -> data.params
                    is StructureData -> data
                    else -> null
                }
                result = structure?.prop(refNode)
                    ?: throw IllegalArgumentException("Container of Arg $refFieldName not found")
            }
            val valueContainer = result
                ?: throw IllegalArgumentException("Container of Arg $refFieldName not found")

            valueContainer.current { it }
        }
        return ResolvedField(
            id = id,
            withUserId = withUserId,
            value = resultValue,
            dataWithUserId = if (withUserId) {
                mapOf(id to resultValue)
            } else {
                mapOf()
            }
        )
    }

    @Suppress("UNCHECKED_CAST")
    override fun mergeDeeply(targetFieldId: String, targetField: Field<*>): Field<ResolvedData> =
        if (targetFieldId == id) {
            if (targetField is ReferenceField) {
                copy(refFieldName = targetField.refFieldName)
            } else {
                targetField.copyWithId(id) as Field<ResolvedData>
            }
        } else {
            this
        }

    override fun copyWithId(id: String): Field<ResolvedData> = copy(id = id)

}