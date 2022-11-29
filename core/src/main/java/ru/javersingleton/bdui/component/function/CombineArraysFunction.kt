package ru.javersingleton.bdui.component.function

import ru.javersingleton.bdui.core.Lambda
import ru.javersingleton.bdui.core.Value
import ru.javersingleton.bdui.core.field.ArrayData
import ru.javersingleton.bdui.core.field.ResolvedData
import ru.javersingleton.bdui.core.field.StructureData
import ru.javersingleton.bdui.core.function.Function

class CombineArraysFunction : Function {
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