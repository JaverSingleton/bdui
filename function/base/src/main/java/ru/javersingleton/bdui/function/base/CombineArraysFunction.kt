package ru.javersingleton.bdui.function.base

import ru.javersingleton.bdui.engine.core.Lambda
import ru.javersingleton.bdui.engine.core.Value
import ru.javersingleton.bdui.engine.field.ArrayData
import ru.javersingleton.bdui.engine.field.ResolvedData
import ru.javersingleton.bdui.engine.field.StructureData
import ru.javersingleton.bdui.engine.function.Function

object CombineArraysFunction : Function {

    override val key: String = "CombineArrays"

    override fun calculate(scope: Lambda.Scope, id: String, params: StructureData): Value<ResolvedData> =
        scope.run {
            rememberValue(id, params) {
                val values: ArrayData? = params.prop("arrays").current()
                ArrayData(
                    id = "$id@result",
                    values?.fields?.flatMap { resolvedField ->
                        when (val data: ResolvedData? = resolvedField.current()) {
                            null -> listOf()
                            is ArrayData -> data.fields
                            else -> listOf(resolvedField)
                        }
                    } ?: listOf()
                )
            }
        }
}